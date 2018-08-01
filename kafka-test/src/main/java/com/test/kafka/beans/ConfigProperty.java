package com.test.kafka.beans;

import java.util.Properties;

public class ConfigProperty {

	private Properties kafkaConsumerProperties;

	private Properties kafkaProducerProperties;
	
	private String kafkaTopic;

	private long kafkaPollInterval;

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

	public long getKafkaPollInterval() {
		return kafkaPollInterval;
	}

	public void setKafkaPollInterval(long kafkaPollInterval) {
		this.kafkaPollInterval = kafkaPollInterval;
	}

}
