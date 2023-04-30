package com.example.couponmsa.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableRedisRepositories
class RedisConfig {
    @Value("\${spring.redis.host}")
    private val redisHost: String? = null

    @Value("\${spring.redis.port}")
    private val redisPort = 0

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Long> {
        val template = RedisTemplate<String, Long>()
        template.setConnectionFactory(redisConnectionFactory)

        val stringSerializer = StringRedisSerializer()
        val longSerializer = GenericToStringSerializer(Long::class.java)


        template.keySerializer = stringSerializer
        template.valueSerializer = longSerializer

        return template
    }
}