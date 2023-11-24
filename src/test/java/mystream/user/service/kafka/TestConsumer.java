package mystream.user.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mystream.user.service.external.event.NewStreamEvent;
import mystream.user.service.external.event.NewStreamFacllbackEvent;

@Service
@Slf4j
public class TestConsumer {
    static private final String TOPIC_CREATE_STREAM = "create-stream";
    static private final String TOPIC_CREATE_STREAM_FALLBACK = "create-stream-fallback";
    static private final String GROUP_ID = "mystream-user";

    @KafkaListener(topics = TOPIC_CREATE_STREAM, groupId = GROUP_ID)
    public void receiveNewStreamEvent(@Payload NewStreamEvent event) {
      log.info("[TEST][receiveNewStreamEvent]result = {}", event);
    }

    @KafkaListener(topics = TOPIC_CREATE_STREAM_FALLBACK, groupId = GROUP_ID)
    public void receiveNewStreamFallbackEvent(@Payload NewStreamFacllbackEvent event) {
      log.info("[TEST][receiveNewStreamFallbackEvent]result = {}", event);
    }

}
