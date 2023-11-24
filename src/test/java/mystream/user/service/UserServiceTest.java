package mystream.user.service;

import static org.mockito.Mockito.timeout;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mystream.user.dto.SignUpDto;
import mystream.user.service.external.event.NewStreamEvent;
import mystream.user.service.external.event.NewStreamFacllbackEvent;
import mystream.user.service.kafka.TestProducer;
import mystream.user.service.kafka.TestConsumer;

@SpringBootTest()
@Import({UserServiceTest.TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@EmbeddedKafka(
  partitions = 1,
  brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" }
)
@Slf4j
public class UserServiceTest {

  @Autowired
  private EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  private UserService userService;

  @SpyBean
  private EventFallbackService eventFallbackService;

  @Captor
  private ArgumentCaptor<NewStreamFacllbackEvent> fallbackEventCap;

  @Autowired
  private TestProducer testProducer;

  @SpyBean
  private TestConsumer testConsumer;

  @Captor
  private ArgumentCaptor<NewStreamEvent> newSteamEventCap;

  @TestConfiguration
  @EnableKafka
  static public class TestConfig {

    @Bean
    ProducerFactory<String, NewStreamFacllbackEvent> producerFactory() {
      Map<String, Object> properties = new HashMap<>();
      properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
      properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
      
      return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    KafkaTemplate<String, NewStreamFacllbackEvent> newproducer() {
      return new KafkaTemplate<String, NewStreamFacllbackEvent>(producerFactory());
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
  public void createUser() {
    String email = "tester@gmail.com";
    String usernmae = "tester";
    String password = "tester";

    SignUpDto dto = new SignUpDto(email, usernmae, password);
    userService.create(dto);

  }

  @Test
  @Order(2)
  public void createUserFallback() {
    String email = "tester@gmail.com";
    String usernmae = "tester";
    String password = "tester";

    // SignUpDto dto = new SignUpDto(email, usernmae, password);
    // UserDto user = userService.create(dto);


    // NewStreamFacllbackEvent fallbackEvent = new NewStreamFacllbackEvent(user.getId());
    // ProducerRecord<String, NewStreamFacllbackEvent> record =
    //   new ProducerRecord<String,NewStreamFacllbackEvent>("create-stream-fallback", fallbackEvent);
    // fallbackProducer.send(record);
    // fallbackProducer.flush();

    testProducer.sendStreamFallback(1L);

    Mockito.verify(eventFallbackService, timeout(3000).times(1))
      .createStreamFallback(fallbackEventCap.capture());

  }

  @Test
  void asoaihds() {
    testProducer.sendStreamFallback(1L);
    
    ArgumentCaptor<NewStreamFacllbackEvent> cap = ArgumentCaptor.forClass(NewStreamFacllbackEvent.class);
    Mockito.verify(testConsumer, timeout(5000).times(1))
      .receiveNewStreamFallbackEvent(cap.capture());
  }
}
