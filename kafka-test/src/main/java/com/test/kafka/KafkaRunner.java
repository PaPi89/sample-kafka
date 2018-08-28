package com.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.test.disruptor.event.PublishKafkaTopicEvent;
import com.test.disruptor.event.ReadKafkaTopicEvent;
import com.test.kafka.beans.ConfigProperty;
import com.test.kafka.common.config.Config;
import com.test.kafka.common.config.PropertiesConfigLoader;
import com.test.kafka.message.processor.MessageProcessor;
import com.test.kafka.message.publisher.MessagePublisher;

public class KafkaRunner implements Runnable {

	private ConfigProperty configProperties;

	private static final String CONFIG_FILE = "config.properties";

	private static final String KAFKA_TOPIC = "kafka.topic";

	private static final String KAFKA_PUBLISH_TOPIC = "kafka.publish.topic";

	private static final String KAFKA_CONSUMER_FILE = "kafka.consumerFilePath";

	private static final String KAFKA_PRODUCER_FILE = "kafka.producerFilePath";

	private String[] cmdArgs;

	public KafkaRunner(String[] args) {
		this.cmdArgs = args;
	}

	public static void main(String[] args) {
		new KafkaRunner(args).run();
	}

	public void run() {

		configProperties = new ConfigProperty();
		configProperties.setKafkaPollInterval(100);

		/** Load Properties **/
		System.out.println("Loading configs...");

		Config config = new Config();

		try {
			config.load(new PropertiesConfigLoader().resource(CONFIG_FILE,
					KafkaRunner.class.getClassLoader()));
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Loaded Configuration:- " + config.toString());

		configProperties.setKafkaTopic(config.getString(KAFKA_TOPIC));
		configProperties.setKafkaPubTopic(config.getString(KAFKA_PUBLISH_TOPIC));

		if (cmdArgs[0].equalsIgnoreCase("consumer")) {

			System.out.println("Loading Consumer Config...");

			try {

				configProperties
						.setKafkaConsumerProperties(new PropertiesConfigLoader()
								.resource(config.getString(KAFKA_CONSUMER_FILE),
										KafkaRunner.class.getClassLoader())
								.load());
				
				configProperties
				.setKafkaProducerProperties(new PropertiesConfigLoader()
						.resource(config.getString(KAFKA_PRODUCER_FILE),
								KafkaRunner.class.getClassLoader())
						.load());
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}

			configProperties.getKafkaConsumerProperties().setProperty(
					ConsumerConfig.MAX_POLL_RECORDS_CONFIG.toString(), "10");

			System.out.println("\nLoaded Consumer Configurations:-\n"
					+ configProperties.getKafkaConsumerProperties());

			/**
			 * Disruptor for reading message from kafka and publish it to ring
			 * buffer
			 **/

			Disruptor<ReadKafkaTopicEvent> readKafkaTopicDisruptor = new Disruptor<>(
					ReadKafkaTopicEvent.EVENT_FACTORY, 1024,
					DaemonThreadFactory.INSTANCE);


			readKafkaTopicDisruptor
					.handleEventsWith(new MessageProcessor(configProperties));

			configProperties.setReadFromkafkaRingBuffer(
					readKafkaTopicDisruptor.start());

			KafkaConsumerGenerator kafkaConsumerGenerator = new KafkaGeneratorBuilder()
					.configProperty(configProperties).buildConsumer();

			kafkaConsumerGenerator.run();
		}

		if (cmdArgs[0].equalsIgnoreCase("producer")) {

			System.out.println("\nLoading Producer Config...");

			try {
				configProperties
						.setKafkaProducerProperties(new PropertiesConfigLoader()
								.resource(config.getString(KAFKA_PRODUCER_FILE),
										KafkaRunner.class.getClassLoader())
								.load());
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}

			System.out.println("\nLoaded Producer Configurations:-\n"
					+ configProperties.getKafkaProducerProperties());

			KafkaProducerGenerator kafkaProducerGenerator = new KafkaGeneratorBuilder()
					.configProperty(configProperties)
					.topic(configProperties.getKafkaTopic()).buildProducer();

			kafkaProducerGenerator.run();
		}

	}

}
