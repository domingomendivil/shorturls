package com.meli.kafka;

/**
 * Custom runtime exception for Kafka 
 */
public class KafkaException extends RuntimeException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KafkaException(Throwable e) {
        super(e);
    }

}
