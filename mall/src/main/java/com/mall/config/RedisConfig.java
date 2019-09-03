package com.mall.config;

import com.mall.pojo.Goods;
import com.mall.pojo.User;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 *@author: yanglvjin
 *@Date: 2019/8/23
 *@Description: redis配置类
 */
@Configuration
public class RedisConfig {

    /**
     * 会员redis模板配置
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public RedisTemplate<Object,User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory)throws UnknownHostException{
        RedisTemplate<Object, User> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        return template;
    }

    /**
     * 商品redis模板配置
     * @param redisConnectionFactory
     * @return
     * @throws UnknownHostException
     */
    @Bean
    public RedisTemplate<Object,Goods> goodsRedisTemplate(RedisConnectionFactory redisConnectionFactory)throws UnknownHostException{
        RedisTemplate<Object,Goods> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Goods.class));
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Goods.class));
        return template;
    }

    /**
     * 会员redis缓存管理器
     * @param userRedisTemplate
     * @return
     */
    @Primary
    @Bean
    public RedisCacheManager userCacheManager(RedisTemplate<Object, User> userRedisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(userRedisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(userRedisTemplate.getValueSerializer()));//设置序列化
        //使用前缀，默认会将CacheName作为key的前缀
        redisCacheConfiguration.usePrefix();
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return cacheManager;
    }

    /**
     * 商品redis缓存管理器
     * @param goodsRedisTemplate
     * @return
     */
    @Bean
    public RedisCacheManager goodsCacheManager(RedisTemplate<Object,Goods> goodsRedisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(goodsRedisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(goodsRedisTemplate.getValueSerializer()));//设置序列化
        //使用前缀，默认会将CacheName作为key的前缀
        redisCacheConfiguration.usePrefix();
        //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return cacheManager;
    }

}
