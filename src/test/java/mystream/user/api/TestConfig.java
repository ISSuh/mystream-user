package mystream.user.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import mystream.user.api.mock.BroadcastServiceMock;
import mystream.user.api.mock.ChannelServiceMock;
import mystream.user.infrastructure.external.BroadcastServiceClient;
import mystream.user.infrastructure.external.ChannelServiceClient;

@TestConfiguration
class TestConfig {

  @Bean
  ChannelServiceClient channelServiceClientMock() {
    return new ChannelServiceMock();
  }

  @Bean
  BroadcastServiceClient broadcastServiceClientMock() {
    return new BroadcastServiceMock();
  }
}
