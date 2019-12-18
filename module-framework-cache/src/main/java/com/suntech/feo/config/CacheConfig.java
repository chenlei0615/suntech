package com.suntech.feo.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo.config
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 13:05
 * ------------    --------------    ---------------------------------
 */
@Configuration
@ComponentScan
public class CacheConfig {
    private StringRedisSerializer stringRedisSerializer () {
        return new StringRedisSerializer();
    }
    @Bean
    public RedisTemplate<String, String> hashAndSetRedisTemplate(
            RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer gjrs = new GenericJackson2JsonRedisSerializer();
        final RedisTemplate template = new RedisTemplate();
        template.setDefaultSerializer(gjrs);
        template.setKeySerializer(template.getStringSerializer());
        template.setHashKeySerializer(template.getStringSerializer());
//
        template.setHashValueSerializer(gjrs);
        template.setValueSerializer(gjrs);
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                // 设置缓存有效期一小时
                .entryTtl(Duration.ofHours(1));

        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }
}
