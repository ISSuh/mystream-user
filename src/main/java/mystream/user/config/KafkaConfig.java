package mystream.user.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import mystream.user.service.external.event.NewStreamEvent;
import mystream.user.service.external.event.NewStreamFacllbackEvent;

@Configuration
@EnableKafka
public class KafkaConfig {
  
  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServer;

  @Bean
  ProducerFactory<String, NewStreamEvent> newStreamEventProducerFactory() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    
    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  KafkaTemplate<String, NewStreamEvent> newStreamEventProducer() {
    return new KafkaTemplate<>(newStreamEventProducerFactory());
  }

  @Bean
  ConsumerFactory<String, NewStreamFacllbackEvent> newStreamFallbackConsumerFactory() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    
    return new DefaultKafkaConsumerFactory<>(properties);
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, NewStreamFacllbackEvent> newStreamFallbackContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, NewStreamFacllbackEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(newStreamFallbackConsumerFactory());
    return factory;
  }
}
