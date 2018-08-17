package com.test.kafka.recovery;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.test.kafka.beans.ConfigProperty;

public class RecoveryService {

	private ConfigProperty configProperty;

	public RecoveryService(ConfigProperty configProperty) {
		this.configProperty = configProperty;
	}

	public void recover(KafkaConsumer<String, String> consumer) {
		KafkaConsumer<String, String> kafkaPubConsumer = new KafkaConsumer<>(
				configProperty.getKafkaConsumerProperties());
		TopicPartition tp = new TopicPartition(
				configProperty.getKafkaPubTopic(), 0);
		kafkaPubConsumer.assign(Arrays.asList(tp));
		kafkaPubConsumer.seekToEnd(Arrays.asList(tp));
		System.out.println(kafkaPubConsumer.position(tp));
		TopicPartition tp1 = new TopicPartition(
				configProperty.getKafkaTopic(), 0);
		consumer.seek(tp1, kafkaPubConsumer.position(tp)+1);
		kafkaPubConsumer.close();
	}
}
