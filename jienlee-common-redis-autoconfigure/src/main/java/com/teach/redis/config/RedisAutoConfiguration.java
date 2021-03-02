package com.teach.redis.config;


import com.teach.core.redis.RedisUtil;
import com.teach.redis.properties.RedisProperties;
import com.teach.redis.serializer.RedisFastJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName ServiceTestAutoConfigure
 * @Description ServiceTestAutoConfigure
 * @Author stack
 * @Version 1.0
 * @since 2019/6/26 22:26
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {


    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisUtil redisUtil(){
        return new RedisUtil();
    }

    /**
     * key 的序列化器
     */
    private final StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();

    /**
     * value 的序列化器
     */
    private final RedisFastJsonSerializer<Object> valueRedisSerializer = new RedisFastJsonSerializer<>(Object.class);


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return jedisConnectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // 值序列化-RedisFastJsonSerializer
        template.setValueSerializer(valueRedisSerializer);
        template.setHashValueSerializer(valueRedisSerializer);
        // 键序列化-StringRedisSerializer
        template.setKeySerializer(keyRedisSerializer);
        template.setHashKeySerializer(keyRedisSerializer);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}