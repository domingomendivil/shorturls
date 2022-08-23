package com.meli.kafka;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.CLIENT_ID_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meli.events.Events;

import lombok.val;

/**
 * Implementation of the Events interface used for sending
 * events to a Kafka instance.
 *
 */
public class KafkaEvents implements Events {

	/*
	 * Topic name used for sending events
	 */
	private final String topicName;

	private final Producer<String, String> producer;

	/*
	 * Indicates whether to enable sending or not events to Kafka
	 */
	private final Boolean enabled;

	/*
	 * Jackson mapper class used to convert a Map<String,String> to a JSON string
	 * for sending to the events layer
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();


	
	public KafkaEvents(Boolean enabled,Producer<String,String> producer,String topicName) {
		this.producer=producer;
		this.enabled=enabled;
		this.topicName=topicName;
	}

	/*
	 * Sends events to Kafka, previously serializing the map properties
	 * to a json structure 
	 */
	@Override
	public void send(final String key,final Map<String, String> value) {
		if (enabled) {
			try {
				val json = objectMapper.writeValueAsString(value);
				val record = new ProducerRecord<String, String>(topicName, key, json);
				producer.send(record);
			} catch (JsonProcessingException e) {
				throw new KafkaException();
			}

		}
	}

}
