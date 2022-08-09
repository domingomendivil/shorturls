package com.meli.kafka;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.meli.events.Events;

public class KafkaEvents implements Events{
    private Properties props;
	
	private String topicName;
	
	private Producer<String,String> producer;
	
	private Boolean enabled;

  public KafkaEvents(Boolean enabled,String brokers,String cliendId,String topicName) {
		this.enabled=enabled;
    this.topicName=topicName;
		if (enabled) {
			props = new Properties();
			props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
			props.put(ProducerConfig.CLIENT_ID_CONFIG, cliendId);
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
			producer = new KafkaProducer<>(props);
		}
	}

  
  @Override
	public void send(String key,String value) {
		if (enabled) {
			var record = new ProducerRecord<String, String>(topicName,key,value);
			producer.send(record);
		}
	}

}
