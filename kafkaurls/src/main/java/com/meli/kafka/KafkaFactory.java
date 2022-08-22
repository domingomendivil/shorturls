package com.meli.kafka;

import lombok.Getter;
import lombok.val;
import shorturls.config.ShortURLProperties;
import shorturls.config.ShortURLPropertiesImpl;

/**
 * Factory class responsible for instantiating the KafkaEvents class
 * 
 *
 */
public class KafkaFactory {
	
	/*
	 * Lombok Getter annotation to facilitate the lazy instantiation of the KafkaEvents class
	 */
	@Getter(lazy=true)
	private static final KafkaEvents instance = init(new ShortURLPropertiesImpl());
	
	/*
	 * Instantiation method of the KafkaEvents class
	 */
	private static final KafkaEvents init(ShortURLProperties props) {
		val brokers = props.getProperty("KAFKA_BROKERS");
		val clientId = props.getProperty("KAFKA_CLIENTID");
		val topicName = props.getProperty("KAFKA_TOPICNAME");
		val enabledStr = props.getProperty("KAFKA_ENABLED");
		val enabled= Boolean.valueOf(enabledStr);
		return new KafkaEvents(enabled,brokers,clientId,topicName);
	}

}
