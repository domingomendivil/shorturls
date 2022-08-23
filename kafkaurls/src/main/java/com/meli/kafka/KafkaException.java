package com.meli.kafka;

/**
 * Custom runtime exception for Kafka 
 */
public class KafkaException extends RuntimeException {

	public KafkaException(Throwable e) {
        super(e);
    }

}
