package com.test.disruptor.event;

import com.lmax.disruptor.EventFactory;

public class ReadKafkaTopicEvent {

	private String message;

	public static final EventFactory<ReadKafkaTopicEvent> EVENT_FACTORY = () -> new ReadKafkaTopicEvent();

	public void set(String message) {
		this.message = message;
	}

	public String get() {
		return this.message;
	}
}
