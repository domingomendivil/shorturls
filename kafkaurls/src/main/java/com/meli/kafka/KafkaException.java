package com.meli.kafka;

public class KafkaException extends RuntimeException {

    public KafkaException(Throwable e) {
        super(e);
    }

}
