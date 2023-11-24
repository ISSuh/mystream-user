package mystream.user.service;

import static org.mockito.Mockito.timeout;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mystream.user.dto.SignUpDto;
import mystream.user.dto.UserDto;
import mystream.user.exceptions.NotFoundException;
import mystream.user.service.external.event.NewStreamEvent;
import mystream.user.service.external.event.NewStreamFacllbackEvent;
import mystream.user.service.kafka.TestConsumer;
import mystream.user.service.kafka.TestProducer;

@SpringBootTest()
@Import({UserServiceTest.TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @SpyBean
  private EventFallbackService eventFallbackService;

  @Autowired
  private TestProducer testProducer;

  @SpyBean
  private TestConsumer testConsumer;

  @TestConfiguration
  @EnableKafka
  static public class TestConfig {

    @Bean
    ProducerFactory<String, NewStreamFacllbackEvent> testProducerFactory() {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);      
      return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    KafkaTemplate<String, NewStreamFacllbackEvent> newproducer() {
      return new KafkaTemplate<String, NewStreamFacllbackEvent>(testProducerFactory());
    }

    @Bean
    ConsumerFactory<String, NewStreamEvent> newStreamConsumerFactory() {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
      
      return new DefaultKafkaConsumerFactory<String, NewStreamEvent>(properties);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, NewStreamEvent> newStreamContainerFactory() {
      ConcurrentKafkaListenerContainerFactory<String, NewStreamEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(newStreamConsumerFactory());
      return factory;
    }

  }

  @Test
  @Order(1)
  public void createUser() throws JsonMappingException, JsonProcessingException {
    String email = "tester1@gmail.com";
    String username = "tester1";
    String password = "tester1";

    SignUpDto dto = new SignUpDto(email, username, password);
    UserDto saved = userService.create(dto);

    ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
    Mockito.verify(testConsumer, timeout(3000).times(1))
      .receiveNewStreamEvent(cap.capture());

    ObjectMapper mapper = new ObjectMapper();
    NewStreamEvent event = mapper.readValue(cap.getValue(), NewStreamEvent.class);
    Assertions.assertThat(event.getId()).isEqualTo(saved.getId());
    Assertions.assertThat(event.getUsername()).isEqualTo(saved.getUsername());
  }

  @Test
  @Order(2)
  public void createUserFallback() throws JsonMappingException, JsonProcessingException {
    String email = "tester2@gmail.com";
    String usernmae = "tester2";
    String password = "tester2";

    SignUpDto dto = new SignUpDto(email, usernmae, password);
    UserDto user = userService.create(dto);

    testProducer.sendStreamFallback(user.getId());

    ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
    Mockito.verify(eventFallbackService, timeout(3000).times(1))
      .createStreamFallback(cap.capture());

    ObjectMapper mapper = new ObjectMapper();
    NewStreamFacllbackEvent event = mapper.readValue(cap.getValue(), NewStreamFacllbackEvent.class);
    Assertions.assertThat(event.getId()).isEqualTo(user.getId());
    
    Assertions.assertThatThrownBy(() -> userService.findUserById(user.getId()))
      .isInstanceOf(NotFoundException.class);
  }

}
