package mystream.user.temp;

// @SpringBootTest()
// @Import({UserServiceTest.TestConfig.class})
// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @Slf4j
public class UserServiceTest {

  // @Autowired
  // private UserService userService;

  // @SpyBean
  // private EventFallbackService eventFallbackService;

  // @Autowired
  // private TestProducer testProducer;

  // @SpyBean
  // private TestConsumer testConsumer;

  // @TestConfiguration
  // @EnableKafka
  // static public class TestConfig {

  //   @Bean
  //   ProducerFactory<String, NewStreamFacllbackEvent> testProducerFactory() {
  //     Map<String, Object> properties = new HashMap<>();
  //     properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
  //     properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
  //     properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);      
  //     return new DefaultKafkaProducerFactory<>(properties);
  //   }

  //   @Bean
  //   KafkaTemplate<String, NewStreamFacllbackEvent> newproducer() {
  //     return new KafkaTemplate<String, NewStreamFacllbackEvent>(testProducerFactory());
  //   }

  //   @Bean
  //   ConsumerFactory<String, NewStreamEvent> newStreamConsumerFactory() {
  //     Map<String, Object> properties = new HashMap<>();
  //     properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
  //     properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
  //     properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  //     properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
      
  //     return new DefaultKafkaConsumerFactory<String, NewStreamEvent>(properties);
  //   }

  //   @Bean
  //   ConcurrentKafkaListenerContainerFactory<String, NewStreamEvent> newStreamContainerFactory() {
  //     ConcurrentKafkaListenerContainerFactory<String, NewStreamEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
  //     factory.setConsumerFactory(newStreamConsumerFactory());
  //     return factory;
  //   }

  // }

  // @Test
  // @Order(1)
  // public void createUser() throws JsonMappingException, JsonProcessingException {
  //   String email = "tester1@gmail.com";
  //   String username = "tester1";
  //   String password = "tester1";

  //   SignUpDto dto = new SignUpDto(email, username, password);
  //   UserDto saved = userService.create(dto);

  //   ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
  //   Mockito.verify(testConsumer, timeout(3000).times(1))
  //     .receiveNewStreamEvent(cap.capture());

  //   ObjectMapper mapper = new ObjectMapper();
  //   NewStreamEvent event = mapper.readValue(cap.getValue(), NewStreamEvent.class);
  //   Assertions.assertThat(event.getId()).isEqualTo(saved.getId());
  //   Assertions.assertThat(event.getUsername()).isEqualTo(saved.getUsername());
  // }

  // @Test
  // @Order(2)
  // public void createUserFallback() throws JsonMappingException, JsonProcessingException {
  //   String email = "tester2@gmail.com";
  //   String usernmae = "tester2";
  //   String password = "tester2";

  //   SignUpDto dto = new SignUpDto(email, usernmae, password);
  //   UserDto user = userService.create(dto);

  //   testProducer.sendStreamFallback(user.getId());

  //   ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
  //   Mockito.verify(eventFallbackService, timeout(3000).times(1))
  //     .createStreamFallback(cap.capture());

  //   ObjectMapper mapper = new ObjectMapper();
  //   NewStreamFacllbackEvent event = mapper.readValue(cap.getValue(), NewStreamFacllbackEvent.class);
  //   Assertions.assertThat(event.getId()).isEqualTo(user.getId());
    
  //   Assertions.assertThatThrownBy(() -> userService.findUserById(user.getId()))
  //     .isInstanceOf(NotFoundException.class);
  // }

}
