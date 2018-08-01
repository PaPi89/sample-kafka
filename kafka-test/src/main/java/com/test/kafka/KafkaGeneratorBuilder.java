package com.test.kafka;

import com.test.kafka.beans.ConfigProperty;

public class KafkaGeneratorBuilder {

	private ConfigProperty configProperties;

	public KafkaGeneratorBuilder configProperty(
			ConfigProperty configProperties) {
		this.configProperties = configProperties;
		return this;
	}

	public RosettaConsumerGenerator buildConsumer() {
		return new RosettaConsumerGenerator(configProperties);
	}
	
	public RosettaProducerGenerator buildProducer() {
		return new RosettaProducerGenerator(configProperties);
	}
}
