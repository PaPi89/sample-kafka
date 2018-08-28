package com.test.kafka.message.processor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.test.disruptor.event.PublishKafkaTopicEvent;
import com.test.disruptor.event.ReadKafkaTopicEvent;
import com.test.kafka.beans.ConfigProperty;
import com.test.kafka.message.publisher.MessagePublisher;

public class MessageProcessor implements EventHandler<ReadKafkaTopicEvent> {

	private ConfigProperty configProperties;

	private Disruptor<PublishKafkaTopicEvent> publishkafkaTopicDisruptor;

	public MessageProcessor(ConfigProperty configProperties) {

		this.configProperties = configProperties;

		this.publishkafkaTopicDisruptor = new Disruptor<>(
				PublishKafkaTopicEvent.EVENT_FACTORY, 1024,
				DaemonThreadFactory.INSTANCE);

		this.publishkafkaTopicDisruptor
				.handleEventsWith(new MessagePublisher(configProperties));

		this.configProperties.setPublishMessageRingBuffer(
				publishkafkaTopicDisruptor.start());

	}

	@Override
	public void onEvent(ReadKafkaTopicEvent event, long seq, boolean endOfBatch)
			throws Exception {

		System.out.println("Event:- " + event + ", Message:- " + event.get()
				+ ", Sequence:- " + seq + ", End of Batch:- " + endOfBatch);

		// Process messages read from kafka topic and send them to another ring
		// buffer.
		long pubSequence = configProperties.getPublishMessageRingBuffer()
				.next();
		PublishKafkaTopicEvent publishMessageEvent = configProperties
				.getPublishMessageRingBuffer().get(pubSequence);
		publishMessageEvent.set(event.get());
		configProperties.getPublishMessageRingBuffer().publish(seq);
	}

}
