package com.test.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import com.test.kafka.beans.ConfigProperty;
import com.test.kafka.recovery.RecoveryService;

public class KafkaConsumerGenerator implements Runnable {

	private ConfigProperty configProperties;

	private KafkaConsumer<String, String> consumer;
	
	private KafkaProducer<String, String> producer;
	
	private RecoveryService recoveryService;

	public KafkaConsumerGenerator(ConfigProperty configProperties) {
		this.configProperties = configProperties;
		this.consumer = new KafkaConsumer<>(
				configProperties.getKafkaConsumerProperties());
		this.producer = new  KafkaProducer<>(
				configProperties.getKafkaProducerProperties());
		this.consumer
				.subscribe(Arrays.asList(configProperties.getKafkaTopic()), consumerRebalanceListener);
		this.recoveryService = new RecoveryService(configProperties);
	}

	public void run() {
		try {
			receiveMessage();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receiveMessage() throws InterruptedException {
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
					producer.send(new ProducerRecord<String, String>(
							configProperties.getKafkaPubTopic(),
							record.value()));
					producer.flush();
				}
				Thread.sleep(5000);
		}
	}

	private final ConsumerRebalanceListener consumerRebalanceListener = new ConsumerRebalanceListener() {
		
		@Override
		public void onPartitionsRevoked(Collection<TopicPartition> arg0) {
			System.out.println("Inside onPartitionsRevoked...");
		}
		
		@Override
		public void onPartitionsAssigned(Collection<TopicPartition> arg0) {
			System.out.println("Inside onPartitionsAssigned...");
			recoveryService.recover(consumer);
		}
	};
}
