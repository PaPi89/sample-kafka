package com.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.test.kafka.beans.ConfigProperty;

public class KafkaProducerGenerator implements Runnable {

	//private ConfigProperty configProperties;

	private String topic;

	private KafkaProducer<String, String> producer;

	public KafkaProducerGenerator(ConfigProperty configProperties,
			String topic) {
		//this.configProperties = configProperties;
		this.topic = topic;
		this.producer = new KafkaProducer<>(
				configProperties.getKafkaProducerProperties());
	}

	@Override
	public void run() {
		for (int i = 0; i < 100; i++) {

			// send lots of messages
			sendMessage(
					String.format("{\"type\":\"test\", \"t\":%.3f, \"k\":%d}",
							System.nanoTime() * 1e-9, i));
			System.out.println("Sent msg number " + i);

		}

		producer.close();
	}

	public void sendMessage(String message) {

		try {
			producer.send(new ProducerRecord<String, String>(topic, message));

			producer.flush();
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
		}

	}
}
