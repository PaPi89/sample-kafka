package com.test.kafka;

import com.test.kafka.beans.ConfigProperty;
import com.test.kafka.common.config.Config;
import com.test.kafka.common.config.PropertiesConfigLoader;

public class KafkaRunner implements Runnable {

	private ConfigProperty configProperties;

	private static final String CONFIG_FILE = "config.properties";

	private static final String KAFKA_TOPIC = "kafka.topic";

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
		
		if(cmdArgs[0].equalsIgnoreCase("consumer")) {
			
			System.out.println("Loading Consumer Config...");
			
			try {
				
				configProperties
				.setKafkaConsumerProperties(new PropertiesConfigLoader()
						.resource(config.getString(KAFKA_CONSUMER_FILE),
								KafkaRunner.class.getClassLoader())
						.load());
			}
			catch (Exception e) {
				e.printStackTrace();
				return;
			}
			
			System.out.println("\nLoaded Consumer Configurations:-\n"
					+ configProperties.getKafkaConsumerProperties());
			
			KafkaConsumerGenerator kafkaConsumerGenerator = new KafkaGeneratorBuilder()
					.configProperty(configProperties).buildConsumer();
			
			kafkaConsumerGenerator.run();
		}

		if(cmdArgs[0].equalsIgnoreCase("producer")) {
			
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
					.configProperty(configProperties).buildProducer();
			
			kafkaProducerGenerator.run();
		}


	}

}
