package com.teach.core.redis;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtil implements ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private static StringRedisTemplate redisTemplate;

    public static String getKey(String groupKey, Object key) {
        return String.format("%s:%s", groupKey, key);
    }

    /**
     * 取得clazz类型的对象
     *
     * @param groupKey 组别
     * @param key key
     * @param clazz clazz
     * @param <T> 犯行
     * @return
     */
    public static <T> T get(final String groupKey, final Object key, final Class<T> clazz) {
        String value =(String) redisTemplate.opsForValue().get(getKey(groupKey, key));
        return toBean(value, clazz);
    }

    public static <T> T toBean(String value, Class<T> tClass) {
        T t = null;
        try {
            t = JSON.parseObject(value, tClass);
        } catch (Exception exception) {
            logger.error("value:{} toBean error!", value);
        }
        return t;
    }

    /**
     * 指定缓存失效时间
     * @param groupKey
     * @param key
     * @param time
     * @return
     */
    public static boolean expire(String groupKey, String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.expire(getKey(groupKey, key), time, timeUnit);
            }
            return true;
        }catch (Exception e) {
            logger.error("RedisUtil.expire failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }


    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public static long getExpire(String groupKey, String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(getKey(groupKey, key), timeUnit);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static boolean hasKey(String groupKey, String key) {
        try {
            return redisTemplate.hasKey(getKey(groupKey, key));
        } catch (Exception e) {
            logger.error("RedisUtil.hasKey failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public static void del(String groupKey, String... key) {
        for (String s : key) {
            s = getKey(groupKey, s);
        }
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    // ============================String=============================

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static void set(String groupKey, String key, Object value) {
        try {
            redisTemplate.opsForValue().set(getKey(groupKey, key), JSON.toJSONString(value));
        } catch (Exception e) {
            logger.error("RedisUtil.setValue failed. groupKey:{} key:{}", groupKey, key, e);
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static void setAndExpire(String groupKey, String key, Object value, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(getKey(groupKey, key), JSON.toJSONString(value), time, timeUnit);
            } else {
                set(groupKey, key, value);
            }
        } catch (Exception e) {
            logger.error("RedisUtil.setValueAndExpire failed. groupKey:{} key:{}", groupKey, key, e);
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static long incr(String groupKey, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(getKey(groupKey, key), delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static long decr(String groupKey, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(getKey(groupKey, key), -delta);
    }

    // ================================Map=================================

    /**
     * HashGet
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static Object hget(String groupKey, String key, String item) {
        return redisTemplate.opsForHash().get(getKey(groupKey, key), item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public static Map<Object, Object> hmget(String groupKey, String key) {
        return redisTemplate.opsForHash().entries(getKey(groupKey, key));
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static boolean hmset(String groupKey, String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(getKey(groupKey, key), map);
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.hmset failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public static boolean hmsetAndExpire(String groupKey, String key, Map<String, Object> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(getKey(groupKey, key), map);
            if (time > 0) {
                expire(groupKey, key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.hmsetAndExpire failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public static boolean hset(String groupKey, String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(getKey(groupKey, key), item, value);
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.hset failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public static boolean hset(String groupKey, String key, String item, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(getKey(groupKey, key), item, value);
            if (time > 0) {
                expire(groupKey, key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.hset failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public static void hdel(String groupKey, String key, Object... item) {
        redisTemplate.opsForHash().delete(getKey(groupKey, key), item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public static boolean hHasKey(String groupKey, String key, String item) {
        return redisTemplate.opsForHash().hasKey(getKey(groupKey, key), item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public static double hincr(String groupKey, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(getKey(groupKey, key), item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public static double hdecr(String groupKey, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(getKey(groupKey, key), item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public static Set<String> sGet(String groupKey, String key) {
        try {
            return redisTemplate.opsForSet().members(getKey(groupKey, key));
        } catch (Exception e) {
            logger.error("RedisUtil.sGet failed. groupKey:{} key:{}", groupKey, key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String groupKey, String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(getKey(groupKey, key), value);
        } catch (Exception e) {
            logger.error("RedisUtil.sHasKey failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSet(String groupKey, String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(getKey(groupKey, key), values);
        } catch (Exception e) {
            logger.error("RedisUtil.sSet failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static long sSetAndTime(String groupKey, String key, long time, TimeUnit timeUnit, String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(getKey(groupKey, key), values);
            if (time > 0){
                expire(groupKey, key, time, timeUnit);
            }
            return count;
        } catch (Exception e) {
            logger.error("RedisUtil.sSetAndTime failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long sGetSetSize(String groupKey, String key) {
        try {
            return redisTemplate.opsForSet().size(getKey(groupKey, key));
        } catch (Exception e) {
            logger.error("RedisUtil.sGetSetSize failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public static long setRemove(String groupKey, String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(getKey(groupKey, key), values);
            return count;
        } catch (Exception e) {
            logger.error("RedisUtil.setRemove failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }
    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public static List<String> lGet(String groupKey, String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(getKey(groupKey, key), start, end);
        } catch (Exception e) {
            logger.error("RedisUtil.lGet failed. groupKey:{} key:{}", groupKey, key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public static long lGetListSize(String groupKey, String key) {
        try {
            return redisTemplate.opsForList().size(getKey(groupKey, key));
        } catch (Exception e) {
            logger.error("RedisUtil.lGetListSize failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public static Object lGetIndex(String groupKey, String key, long index) {
        try {
            return redisTemplate.opsForList().index(getKey(groupKey, key), index);
        } catch (Exception e) {
            logger.error("RedisUtil.lGetIndex failed. groupKey:{} key:{}", groupKey, key, e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String groupKey, String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(getKey(groupKey, key), JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.lSet failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String groupKey, String key, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForList().rightPush(getKey(groupKey, key), JSON.toJSONString(value));
            if (time > 0){
                expire(groupKey, key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.lSet failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static boolean lSet(String groupKey, String key, List<String> value) {
        try {
            redisTemplate.opsForList().rightPushAll(getKey(groupKey, key), value);
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.lSet failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public static boolean lSet(String groupKey, String key, List<String> value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForList().rightPushAll(getKey(groupKey, key), value);
            if (time > 0){
                expire(groupKey, key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.lSet failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static boolean lUpdateIndex(String groupKey, String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(getKey(groupKey, key), index, JSON.toJSONString(value));
            return true;
        } catch (Exception e) {
            logger.error("RedisUtil.lUpdateIndex failed. groupKey:{} key:{}", groupKey, key, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static long lRemove(String groupKey, String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(getKey(groupKey, key), count, value);
            return remove;
        } catch (Exception e) {
            logger.error("RedisUtil.lRemove failed. groupKey:{} key:{}", groupKey, key, e);
            return 0;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        redisTemplate = (StringRedisTemplate) context.getBean("stringRedisTemplate");
    }
}
