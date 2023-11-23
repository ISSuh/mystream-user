package mystream.user.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import mystream.user.entity.User;
import mystream.user.exceptions.InvalidSignupException;
import mystream.user.repository.UserRepository;
import mystream.user.service.external.event.NewStreamFallbackEvent;

@Service
@RequiredArgsConstructor
public class EventFallbackService {

  static private final String TOPIC_CREATE_STREAM_FALLBACK = "create-stream-fallback";
  static private final String GROUP_ID = "mystream-user";
  
  private final UserRepository userRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = TOPIC_CREATE_STREAM_FALLBACK, groupId = GROUP_ID)
  public void createStreamFallback(String fallbackJson) {
    NewStreamFallbackEvent event = null;

    try {
      event = objectMapper.readValue(fallbackJson, NewStreamFallbackEvent.class);
    } catch (JsonProcessingException e) {
      throw new InvalidSignupException(e.getMessage());
    }

    Optional<User> user = userRepository.findById(event.getId());
    if (user.isPresent()) {
      userRepository.delete(user.get());
    }
  }

}
