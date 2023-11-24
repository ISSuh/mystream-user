package mystream.user.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

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

  @KafkaListener(topics = TOPIC_CREATE_STREAM_FALLBACK, groupId = GROUP_ID)
  public void createStreamFallback(@Payload NewStreamFacllbackEvent event) {
    log.info("[createStreamFallback]payload = {}", event);

    Optional<User> user = userRepository.findById(event.getId());
    if (user.isPresent()) {
      userRepository.delete(user.get());
    }
  }

}
