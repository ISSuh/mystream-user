package mystream.user.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import mystream.user.dto.NewStreamDto;

@Configuration
@EnableKafka
public class KafkaConfig {
  
  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServer;

  @Bean
  ProducerFactory<String, NewStreamDto> newStreamDtoProducerFactory() {
    Map<String, Object> properties = new HashMap<>();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    
    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  KafkaTemplate<String, NewStreamDto> newStreamDtoProducer() {
    return new KafkaTemplate<>(newStreamDtoProducerFactory());
  }
}
