package com.meli.kafka.config;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;

import com.meli.kafka.KafkaEvents;

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
	private static final KafkaEvents instance = init(ShortURLPropertiesImpl.getInstance());
	
	
	
	/*
	 * Instantiation method of the KafkaEvents class
	 */
	private static final KafkaEvents init(ShortURLProperties shortProps) {
		val brokers = shortProps.getProperty("KAFKA_BROKERS");
		val clientId = shortProps.getProperty("KAFKA_CLIENTID");
		val topicName = shortProps.getProperty("KAFKA_TOPICNAME");
		val enabledStr = shortProps.getProperty("KAFKA_ENABLED");
		val enabled= Boolean.valueOf(enabledStr);
		Properties props = new Properties();;
		props = new Properties();
		props.put(BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(CLIENT_ID_CONFIG, clientId);
		val name= StringSerializer.class.getName();
		props.put(KEY_SERIALIZER_CLASS_CONFIG, name);
		props.put(VALUE_SERIALIZER_CLASS_CONFIG, name);
		Producer<String, String> producer = new KafkaProducer<>(props);
		
		return new KafkaEvents(enabled,producer,topicName);
	}

}
