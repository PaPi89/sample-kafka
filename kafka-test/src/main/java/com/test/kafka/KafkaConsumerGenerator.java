package com.test.kafka;

import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.test.kafka.beans.ConfigProperty;

public class KafkaConsumerGenerator implements Runnable {

	private ConfigProperty configProperties;

	private KafkaConsumer<String, String> consumer;

	public KafkaConsumerGenerator(ConfigProperty configProperties) {
		this.configProperties = configProperties;
		this.consumer = new KafkaConsumer<>(
				configProperties.getKafkaConsumerProperties());
		this.consumer
				.subscribe(Arrays.asList(configProperties.getKafkaTopic()));
	}

	public void run() {
		receiveMessage();
	}

	private void receiveMessage() {
		int timeouts = 0;

		while (true) {
			// read records with a short timeout. If we time out, we don't
			// really care.
			ConsumerRecords<String, String> records = consumer.poll(
					Duration.ofMillis(configProperties.getKafkaPollInterval()));

			if (records.count() == 0) {
				timeouts++;
			}
			else {
				System.out.printf("Got %d records after %d timeouts\n",
						records.count(), timeouts);
				timeouts = 0;
			}
			for (ConsumerRecord<String, String> record : records) {
				System.out.println(record.value());
			}
		}
	}

}
