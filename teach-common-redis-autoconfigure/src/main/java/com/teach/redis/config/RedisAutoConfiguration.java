package com.teach.redis.config;


import com.teach.core.redis.RedisUtil;
import com.teach.redis.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName ServiceTestAutoConfigure
 * @Description ServiceTestAutoConfigure
 * @Author stack
 * @Version 1.0
 * @since 2019/6/26 22:26
 */
@Configuration
@ConditionalOnClass(RedisProperties.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    @Autowired private RedisProperties redisProperties;

    @Bean
    public RedisUtil redisUtil() {
        return new RedisUtil();
    }


    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory taskConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);

        //配置连接池属性
        connectionFactory.setTimeout(Integer.valueOf(redisProperties.getConfig().getTimeout()));
        connectionFactory.getPoolConfig().setMaxIdle(redisProperties.getConfig().getMaxIdle());
        connectionFactory.getPoolConfig().setMaxTotal(redisProperties.getConfig().getMaxTotal());
        connectionFactory.getPoolConfig().setMaxWaitMillis(redisProperties.getConfig().getMaxWaitMillis());
        return connectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(taskConnectionFactory());
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(taskConnectionFactory());
        return template;
    }

}