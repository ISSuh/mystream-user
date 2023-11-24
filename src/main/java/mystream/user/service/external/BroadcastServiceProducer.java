package mystream.user.service.external;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mystream.user.service.external.event.NewStreamEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class BroadcastServiceProducer {

  static private final String TOPIC_CREATE_STREAM = "create-stream";

  private final KafkaTemplate<String, NewStreamEvent> createStreamTemplate;

  public void createStream(NewStreamEvent event) {
    CompletableFuture<SendResult<String, NewStreamEvent>> future = createStreamTemplate.send(TOPIC_CREATE_STREAM, event);
    future.whenComplete(
      (result, e) -> {
          log.info("[TEST][createStream]result = {}, e = {}", result, e.getMessage());
      }
    );

    createStreamTemplate.flush();
  }
}
