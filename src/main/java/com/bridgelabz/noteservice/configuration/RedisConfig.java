package com.bridgelabz.noteservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;



/**
 * @author yuga
 * @since 19/07/2018
 *
 */
/*@Configuration
@Component
public class RedisConfig {
	*//**
	 * @return jedisConFactory
	 *//*
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		//jedisConFactory.setHostName("localhost");
		//jedisConFactory.setPort(6379);
		return jedisConFactory;
	}

	*//**
	 * @param <T>
	 * @return redis template
	 *//*
	@Bean
	public <T> RedisTemplate <String, T> redisTemplate() {
		RedisTemplate<String, T> redisTemplate = new RedisTemplate<String, T>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
}
*/
