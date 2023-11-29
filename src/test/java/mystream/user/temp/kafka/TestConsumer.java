package mystream.user.temp.kafka;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestConsumer {
  // static private final String TOPIC_CREATE_STREAM = "create-stream";
  // static private final String TOPIC_CREATE_STREAM_FALLBACK = "create-stream-fallback";
  // static private final String GROUP_ID = "mystream-user-test";

  // private CountDownLatch latch = new CountDownLatch(1);

  // @KafkaListener(topics = TOPIC_CREATE_STREAM, groupId = GROUP_ID)
  // public void receiveNewStreamEvent(@Payload String event) {
  //   log.info("[TEST][receiveNewStreamEvent]result = {}", event);
  // }

  // @KafkaListener(topics = TOPIC_CREATE_STREAM_FALLBACK, groupId = GROUP_ID)
  // public void receiveNewStreamFallbackEvent(@Payload String event) {
  //   log.info("[TEST][receiveNewStreamFallbackEvent]result = {}", event);

  //   latch.countDown();
  // }

  // public void resetLatch() {
  //   latch = new CountDownLatch(1);
  // }

  // public CountDownLatch geLatch() {
  //   return this.latch;
  // }

}
