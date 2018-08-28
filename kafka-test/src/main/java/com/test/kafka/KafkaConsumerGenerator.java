package com.test.kafka;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import com.lmax.disruptor.RingBuffer;
import com.test.disruptor.event.ReadKafkaTopicEvent;
import com.test.kafka.beans.ConfigProperty;
import com.test.kafka.recovery.RecoveryService;

public class KafkaConsumerGenerator implements Runnable {

	private ConfigProperty configProperties;

	private KafkaConsumer<String, String> consumer;

	private RecoveryService recoveryService;

	private RingBuffer<ReadKafkaTopicEvent> ringBuffer;

	public KafkaConsumerGenerator(ConfigProperty configProperties) {
		this.configProperties = configProperties;
		this.consumer = new KafkaConsumer<>(
				configProperties.getKafkaConsumerProperties());
		this.ringBuffer = configProperties.getReadFromkafkaRingBuffer();
		this.consumer.subscribe(Arrays.asList(configProperties.getKafkaTopic()),
				consumerRebalanceListener);
		/*
		 * TopicPartition tp = new
		 * TopicPartition(configProperties.getKafkaTopic(), 0);
		 * this.consumer.assign(Arrays.asList(tp));
		 */
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
				//System.out.println(record.value());

				/**
				 * Disruptor code to read message from kafka and publish it to
				 * ring buffer.
				 **/
				long seq = ringBuffer.next();
				ReadKafkaTopicEvent readMessageEvent = ringBuffer.get(seq);
				readMessageEvent.set(record.value());
				ringBuffer.publish(seq);

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
