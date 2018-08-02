package com.test.kafka;

import com.test.kafka.beans.ConfigProperty;

public class KafkaGeneratorBuilder {

	private ConfigProperty configProperties;

	public KafkaGeneratorBuilder configProperty(
			ConfigProperty configProperties) {
		this.configProperties = configProperties;
		return this;
	}

	public KafkaConsumerGenerator buildConsumer() {
		return new KafkaConsumerGenerator(configProperties);
	}
	
	public KafkaProducerGenerator buildProducer() {
		return new KafkaProducerGenerator(configProperties);
	}
}
