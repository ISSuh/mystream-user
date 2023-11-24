package mystream.user.service.kafka;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mystream.user.service.external.event.NewStreamFacllbackEvent;

@Service
@Slf4j
public class TestProducer {
  static private final String TOPIC_CREATE_STREAM = "create-stream";
  static private final String TOPIC_CREATE_STREAM_FALLBACK = "create-stream-fallback";

  private final KafkaTemplate<String, NewStreamFacllbackEvent> eventTemplate;

  public TestProducer(KafkaTemplate<String, NewStreamFacllbackEvent> template) {
    this.eventTemplate = template;
  }

  public void sendStreamFallback(Long id) {
    NewStreamFacllbackEvent event = new NewStreamFacllbackEvent(id); 
    CompletableFuture<SendResult<String, NewStreamFacllbackEvent>> future = eventTemplate.send(TOPIC_CREATE_STREAM_FALLBACK, event);
    future.whenComplete(
      (result, e) -> {
        log.info("[TEST]result = {}", result);
        
      });
    eventTemplate.flush();
  }


}

