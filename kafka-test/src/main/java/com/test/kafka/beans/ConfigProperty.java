package com.test.kafka.beans;

import java.util.Properties;

import com.lmax.disruptor.RingBuffer;
import com.test.disruptor.event.PublishKafkaTopicEvent;
import com.test.disruptor.event.ReadKafkaTopicEvent;

public class ConfigProperty {

	private Properties kafkaConsumerProperties;

	private Properties kafkaProducerProperties;
	
	private String kafkaTopic;
	
	private String kafkaPubTopic;

	private long kafkaPollInterval;
	
	private RingBuffer<ReadKafkaTopicEvent> readFromkafkaRingBuffer;
	
	private RingBuffer<PublishKafkaTopicEvent> publishMessageRingBuffer;
	
	public Properties getKafkaConsumerProperties() {
		return kafkaConsumerProperties;
	}

	public void setKafkaConsumerProperties(Properties kafkaConsumerProperties) {
		this.kafkaConsumerProperties = kafkaConsumerProperties;
	}

	public Properties getKafkaProducerProperties() {
		return kafkaProducerProperties;
	}

	public void setKafkaProducerProperties(Properties kafkaProducerProperties) {
		this.kafkaProducerProperties = kafkaProducerProperties;
	}

	public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	public String getKafkaPubTopic() {
		return kafkaPubTopic;
	}

	public void setKafkaPubTopic(String kafkaPubTopic) {
		this.kafkaPubTopic = kafkaPubTopic;
	}

	public long getKafkaPollInterval() {
		return kafkaPollInterval;
	}

	public void setKafkaPollInterval(long kafkaPollInterval) {
		this.kafkaPollInterval = kafkaPollInterval;
	}

	public RingBuffer<ReadKafkaTopicEvent> getReadFromkafkaRingBuffer() {
		return readFromkafkaRingBuffer;
	}

	public void setReadFromkafkaRingBuffer(
			RingBuffer<ReadKafkaTopicEvent> readFromkafkaRingBuffer) {
		this.readFromkafkaRingBuffer = readFromkafkaRingBuffer;
	}

	public RingBuffer<PublishKafkaTopicEvent> getPublishMessageRingBuffer() {
		return publishMessageRingBuffer;
	}

	public void setPublishMessageRingBuffer(
			RingBuffer<PublishKafkaTopicEvent> publishMessageRingBuffer) {
		this.publishMessageRingBuffer = publishMessageRingBuffer;
	}

}
