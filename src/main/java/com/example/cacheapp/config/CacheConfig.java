package com.example.cacheapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

	@CacheEvict(
			allEntries = true,
			cacheNames = {
					"users",
					"products",
					"attendance",
					"attendance_system_report"
			})
	@Scheduled(fixedDelay = 9000000)
	public void cacheEvict() {
		log.info("Evicting all cache entries");
	}


	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		objectMapper.activateDefaultTyping(
				objectMapper.getPolymorphicTypeValidator(),
				ObjectMapper.DefaultTyping.NON_FINAL
		);

		GenericJackson2JsonRedisSerializer serializer =
				new GenericJackson2JsonRedisSerializer(objectMapper);

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(serializer)
				)
				.disableCachingNullValues()
				.entryTtl(Duration.ofMinutes(10));

		return RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(config)
				.build();
	}
}
