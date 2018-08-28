package com.test.kafka;

import com.test.kafka.beans.ConfigProperty;

public class KafkaGeneratorBuilder {

	private ConfigProperty configProperties;
	
	private String topic;

	public KafkaGeneratorBuilder configProperty(
			ConfigProperty configProperties) {
		this.configProperties = configProperties;
		return this;
	}
	
	public KafkaGeneratorBuilder topic(String topic) {
		this.topic = topic;
		return this;
	}

	public KafkaConsumerGenerator buildConsumer() {
		return new KafkaConsumerGenerator(configProperties);
	}
	
	public KafkaProducerGenerator buildProducer() {
		return new KafkaProducerGenerator(configProperties, topic);
	}
}
