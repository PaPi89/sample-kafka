package com.test.disruptor.event;

import com.lmax.disruptor.EventFactory;

public class PublishKafkaTopicEvent {

	private String message;

	public static final EventFactory<PublishKafkaTopicEvent> EVENT_FACTORY = () -> new PublishKafkaTopicEvent();

	public void set(String message) {
		this.message = message;
	}

	public String get() {
		return this.message;
	}
}
