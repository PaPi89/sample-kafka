package com.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.test.kafka.beans.ConfigProperty;

public class KafkaProducerGenerator implements Runnable {

	private ConfigProperty configProperties;

	private KafkaProducer<String, String> producer;

	public KafkaProducerGenerator(ConfigProperty configProperties) {
		this.configProperties = configProperties;
		this.producer = new KafkaProducer<>(
				configProperties.getKafkaProducerProperties());
	}

	@Override
	public void run() {
		sendMessage();
	}

	private void sendMessage() {

		try {
			for (int i = 0; i < 1000000; i++) {
				// send lots of messages
				producer.send(new ProducerRecord<String, String>(
						configProperties.getKafkaTopic(),
						String.format(
								"{\"type\":\"test\", \"t\":%.3f, \"k\":%d}",
								System.nanoTime() * 1e-9, i)));

				producer.flush();
				System.out.println("Sent msg number " + i);
			}
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		finally {
			producer.close();
		}

	}
}
