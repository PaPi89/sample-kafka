package com.test.kafka.message.publisher;

import com.lmax.disruptor.EventHandler;
import com.test.disruptor.event.PublishKafkaTopicEvent;
import com.test.kafka.KafkaGeneratorBuilder;
import com.test.kafka.KafkaProducerGenerator;
import com.test.kafka.beans.ConfigProperty;

public class MessagePublisher implements EventHandler<PublishKafkaTopicEvent>{

	KafkaProducerGenerator kafkaProducerGenerator;
	
	public MessagePublisher(ConfigProperty configProperties) {
		this.kafkaProducerGenerator = new KafkaGeneratorBuilder()
				.configProperty(configProperties)
				.topic(configProperties.getKafkaPubTopic()).buildProducer();
	}

	@Override
	public void onEvent(PublishKafkaTopicEvent event, long seq, boolean endOfBatch)
			throws Exception {
		
		kafkaProducerGenerator.sendMessage(event.get());
	}
}
