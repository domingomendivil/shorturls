package com.meli.kafka;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;


public class KafkaEventsTest {
	
	@SuppressWarnings("unchecked")
	private Producer<String,String> producer= mock(Producer.class);

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void test1() throws JsonProcessingException {
		KafkaEvents events = new KafkaEvents(true,producer,"topicName1");
		Map<String, String> map = new HashMap<>();
		map.put("user-agent", "firefox");
		events.send("key",map);
		String json = objectMapper.writeValueAsString(map);
		val record = new ProducerRecord<String, String>("topicName1", "key", json);
		verify(producer).send(record);
	}
	
	@Test
	public void test2() throws JsonProcessingException {
		KafkaEvents events = new KafkaEvents(false,producer,"topicName1");
		Map<String, String> map = new HashMap<>();
		map.put("user-agent", "firefox");
		events.send("key",map);
		String json = objectMapper.writeValueAsString(map);
		val record = new ProducerRecord<String, String>("topicName1", "key", json);
		verify(producer,Mockito.never()).send(record);
	}

}
