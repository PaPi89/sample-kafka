package com.test.kafka.recovery;

import java.time.Duration;
import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.test.kafka.beans.ConfigProperty;

public class RecoveryService {

	private ConfigProperty configProperty;
	
	private long offset;
	
	public RecoveryService(ConfigProperty configProperty) {
		this.configProperty = configProperty;
	}

	public KafkaConsumer<String, String> recover(KafkaConsumer<String, String> consumer) {
		KafkaConsumer<String, String> kafkaPubConsumer = new KafkaConsumer<>(
				configProperty.getKafkaConsumerProperties());
		TopicPartition tp = new TopicPartition(
				configProperty.getKafkaPubTopic(), 0);
		kafkaPubConsumer.assign(Arrays.asList(tp));
		kafkaPubConsumer.seekToEnd(Arrays.asList(tp));
		System.out.println("Actual Position:- " + kafkaPubConsumer.position(tp));
		System.out.println("Position We need:- " + offset);
		TopicPartition tp1 = new TopicPartition(
				configProperty.getKafkaTopic(), 0);
		consumer.seek(tp1, offset + 1);
		kafkaPubConsumer.close();
		return consumer;
	}
	
	public boolean recover() {
		int timeout = 0;
		KafkaConsumer<String, String> kafkaPubConsumer = new KafkaConsumer<>(
				configProperty.getKafkaConsumerProperties());
		TopicPartition tp = new TopicPartition(
				configProperty.getKafkaPubTopic(), 0);
		kafkaPubConsumer.assign(Arrays.asList(tp));
		boolean result = true;
		while(true) {
			ConsumerRecords<String, String> records = kafkaPubConsumer.poll(
					Duration.ofMillis(configProperty.getKafkaPollInterval()));
			if (records.count() == 0) {
				result = false;
				timeout++;
			} else {
				timeout = 0;
			}
			for (ConsumerRecord<String, String> record : records) {
				if(record.value().equals("HEARTBEAT")) {
					System.out.println("false");
					result = false;
				} else {
					String[] tokens = record.value().split("offset");
					System.out.println(tokens[1]);
					offset =  Long.parseLong(tokens[1]);
				}
			}
			if(timeout > 100) {
				System.out.println("Timeout....");
				result = true;
				break;
			}
		}
		kafkaPubConsumer.close();
		return result;
	}
	
}
