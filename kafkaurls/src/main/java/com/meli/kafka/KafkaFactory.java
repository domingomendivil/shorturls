package com.meli.kafka;

import lombok.Getter;
import shorturls.config.ShortURLProperties;

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
	private static final KafkaEvents instance = init(new ShortURLProperties());
	
	/*
	 * Instantiation method of the KafkaEvents class
	 */
	private static final KafkaEvents init(ShortURLProperties props) {
		String brokers = props.getProperty("KAFKA_BROKERS");
		String clientId = props.getProperty("KAFKA_CLIENTID");
		String topicName = props.getProperty("KAFKA_TOPICNAME");
		String enabledStr = props.getProperty("KAFKA_ENABLED");
		Boolean enabled= Boolean.valueOf(enabledStr);
		return new KafkaEvents(enabled,brokers,clientId,topicName);
	}

}
