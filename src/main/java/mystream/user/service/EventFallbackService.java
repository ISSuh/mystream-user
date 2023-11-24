package mystream.user.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mystream.user.entity.User;
import mystream.user.repository.UserRepository;
import mystream.user.service.external.event.NewStreamFacllbackEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventFallbackService {

  static private final String TOPIC_CREATE_STREAM_FALLBACK = "create-stream-fallback";
  static private final String GROUP_ID = "mystream-user";
    
  private final UserRepository userRepository;
  private final ObjectMapper mapper = new ObjectMapper();

  @KafkaListener(topics = TOPIC_CREATE_STREAM_FALLBACK, groupId = GROUP_ID)
  public void createStreamFallback(@Payload String eventStr) {
    log.info("[createStreamFallback]payload = {}", eventStr);

    NewStreamFacllbackEvent event = null;
    try {
      event = mapper.readValue(eventStr, NewStreamFacllbackEvent.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException();
    }

    Optional<User> user = userRepository.findById(event.getId());
    if (user.isPresent()) {
      userRepository.delete(user.get());
    }
  }

}
