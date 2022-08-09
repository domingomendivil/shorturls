package com.meli.kafka;

public class KafkaFactory {
	
	private static KafkaEvents kafkaEvents;
	
	private static final String BROKERS = System.getenv("KAFKA_BROKERS");
	private static final String CLIENT_ID = System.getenv("KAFKA_CLIENTID");
	private static final String TOPIC_NAME = System.getenv("KAFKA_TOPICNAME");
	private static final String ENABLED = System.getenv("KAFKA_ENABLED");
	
	public static synchronized KafkaEvents getInstance() {
		if (kafkaEvents==null) {
			Boolean enabled= Boolean.valueOf(ENABLED);
			kafkaEvents = new KafkaEvents(enabled,BROKERS,CLIENT_ID,TOPIC_NAME);
		}
		return kafkaEvents;
	}

}
