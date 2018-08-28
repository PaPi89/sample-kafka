package com.test.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import com.test.disruptor.event.ReadKafkaTopicEvent;

public class ReadKafkaTopicEventProducer {

	private final RingBuffer<ReadKafkaTopicEvent> ringBuffer;

	public ReadKafkaTopicEventProducer(
			RingBuffer<ReadKafkaTopicEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void readFromKafkaTopic(String msg) {

		long seq = ringBuffer.next();
		try {
			ReadKafkaTopicEvent readEvent = ringBuffer.get(seq);
			readEvent.set(msg);
		}
		finally {
			ringBuffer.publish(seq);
		}
	}
}
