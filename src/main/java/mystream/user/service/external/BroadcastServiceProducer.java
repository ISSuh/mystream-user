package mystream.user.service.external;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mystream.user.service.external.event.NewStreamEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BroadcastServiceProducer {

  static private final String TOPIC_CREATE_STREAM = "create-stream";

  private final KafkaTemplate<String, String> createStreamTemplate;

  public void createStream(NewStreamEvent event) {
    ObjectMapper mapper = new ObjectMapper();
    String eventStr = null;
    
    try {
      eventStr = mapper.writeValueAsString(event);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException();
    }

    log.info("[TEST][createStream] before = {}", eventStr);
    CompletableFuture<SendResult<String, String>> future = createStreamTemplate.send(TOPIC_CREATE_STREAM, eventStr);
    future.whenComplete(
      (result, e) -> {
          log.info("[TEST][createStream]result = {}, e = {}", result, e.getMessage());
      }
    );

    createStreamTemplate.flush();
  }
}
