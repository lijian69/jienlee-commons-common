package com.teach.core.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.ResponseCode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.teach.core.spring.SpringUtils;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yangyongxin
 */
public class RocketMQProducerHelper {

	private static Logger logger = LoggerFactory.getLogger(RocketMQProducerHelper.class);
    private static Map<EnvEnum, RocketMQProducerHelper> helperMap = new EnumMap<EnvEnum, RocketMQProducerHelper>(EnvEnum.class);
	private DefaultMQProducer producer = null;
	private Queue<Message> retryQueue = new ConcurrentLinkedQueue<>();
	private int type = 0;
	private TimerTask retryTask;
	private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
	private LoadingCache<String, List> qSizeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build(new CacheLoader<String, List>() {
                @Override
                public List load(@NonNull String key) throws Exception {
                    return producer.fetchPublishMessageQueues(key);
                }
            });

	private RocketMQProducerHelper() {
	}

    private RocketMQProducerHelper(int type) {
        this.type = type;
        init();
        startDaemon();
        addShutdownHook();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (retryTask != null){
                    retryTask.run();
                }
                if (producer != null){
                    producer.shutdown();
                }
            }
        }));
    }

    public static RocketMQProducerHelper getInstance() {
	    return getInstance(EnvEnum.DEFAULT.value);
    }

    /**
     * 实例化send mq对象
     * @param type 1：DEFAULT, 2: ONLINE
     * @return
     */
    public static RocketMQProducerHelper getInstance(int type) {
        if (EnvEnum.valueOf(type) == null) {
            logger.error("RocketMQProducerHelper.getInstance param is miss!");
            return null;
        }
        if (helperMap.get(EnvEnum.valueOf(type)) == null) {
            synchronized (RocketMQProducerHelper.class) {
                if (helperMap.get(EnvEnum.valueOf(type)) == null) {
                    helperMap.put(EnvEnum.valueOf(type), new RocketMQProducerHelper(type));
                }
            }
        }
        return helperMap.get(EnvEnum.valueOf(type));
    }

	private void init(){
        EnvEnum en = EnvEnum.valueOf(type);
        if (en == null) {
            logger.error("DefaultProducer start failed. type is not exist type = ", type);
            producer = null;
        }

        String addr = SpringUtils.getBean(RocketMQProducerConfig.class).getNamesrvAddr(en);

        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("P_DIDA_INDEX_" + en.toString().toUpperCase());
		defaultMQProducer.setNamesrvAddr(addr);
        defaultMQProducer.setInstanceName("P_DIDA_INDEX_" + en.toString().toUpperCase());
		defaultMQProducer.setRetryAnotherBrokerWhenNotStoreOK(true);
		defaultMQProducer.setCompressMsgBodyOverHowmuch(1024 * 10);
		try {
			defaultMQProducer.start();
		} catch (Exception e) {
			logger.error("DefaultProducer start failed.", e);
			defaultMQProducer = null;
		}
		producer = defaultMQProducer;
	}

	private void startDaemon() {
        retryTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (retryQueue.size() <= 0) {
                        return;
                    }
                    Iterator<Message> iterator = retryQueue.iterator();
                    if(retryQueue.size() >= 100) {

                    }
                    while(iterator.hasNext()) {
                        try {
                            Message msg = iterator.next();
                            producer.send(msg);
                            logger.warn("RocketMQProducerHelper.retry send msg: {}", msg);
                        } catch (Exception e) {
                            continue;
                        }
                        iterator.remove();
                    }
                }catch (Exception e){
                    logger.error("TimerTask failed.", e);
                }
            }
        };
        executorService.scheduleAtFixedRate(retryTask, 100, 1000, TimeUnit.MILLISECONDS);//1s 执行一次
	}

	public SendResult send(Message msg) throws MQClientException {
		if (StringUtils.isEmpty(msg.getTopic())) {
			throw new MQClientException(ResponseCode.TOPIC_NOT_EXIST, "the topic is null");
		}

		if(producer == null){
			throw new MQClientException(ResponseCode.SERVICE_NOT_AVAILABLE, "can't connect service");
		}

		if (!StringUtils.isEmpty(msg.getKeys())){
		    return hSend(msg);
        }

		SendResult result = null;
		try {
			result = producer.send(msg);
            logger.debug("send success! NamesrvAddr:{},Message:{}", producer.getNamesrvAddr(), msg);
        } catch (Exception e) {
			logger.error("RocketMQProducerHelper.send failed.", e);
			retryQueue.add(msg);
		}
		return result;
	}

    /**.
     * 同一Id会发送到同一queue保证顺序同步
     * @param msg
     * @return
     * @throws MQClientException
     */
    public SendResult hSend(Message msg) throws MQClientException {
        return hSend(msg, msg.getKeys());
    }

    /**.
     * 同一Id会发送到同一queue保证顺序同步
     * @param msg
     * @return
     * @throws MQClientException
     */
    public SendResult hSend(Message msg, String orderby) throws MQClientException {
        if (StringUtils.isEmpty(msg.getTopic()) || StringUtils.isEmpty(msg.getKeys())) {
            throw new MQClientException(ResponseCode.TOPIC_NOT_EXIST, "the topic is null");
        }

        if(producer == null){
            throw new MQClientException(ResponseCode.SERVICE_NOT_AVAILABLE, "can't connect service");
        }

        SendResult result = null;
        try {
            long start = System.currentTimeMillis();
            List<MessageQueue> queues = qSizeCache.get(msg.getTopic());
            int selectedQueueIndex = Math.abs(orderby.hashCode() % (queues.size()));
            logger.debug("msgKey:{} sendToQueueIndex:{}", msg.getKeys(), selectedQueueIndex);
            start = System.currentTimeMillis();
            result = producer.send(msg, queues.get(selectedQueueIndex));
        } catch (Exception e) {
            logger.error("RocketMQProducerHelper.send failed. msg: {}", msg, e);
            retryQueue.add(msg);
        }

        return result;
    }

	public synchronized static void refresh(){
        for(Map.Entry<EnvEnum, RocketMQProducerHelper> entry : helperMap.entrySet()){
            entry.getValue().init();
        }
    }

    public enum EnvEnum {
        DEFAULT(1),
        ONLINE(2),
        ;
        private int value;
        EnvEnum(int i) {
            this.value = i;
        }
        public int getValue(){
            return value;
        }
        public static EnvEnum valueOf(int type) {
            for (EnvEnum en : EnvEnum.values()) {
                if (en.value == type) {
                    return en;
                }
            }
            return null;
        }
    }

}
