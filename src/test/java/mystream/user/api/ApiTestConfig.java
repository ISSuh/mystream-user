package mystream.user.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import mystream.user.api.mock.BroadcastServiceMock;
import mystream.user.api.mock.ChannelServiceMock;
import mystream.user.infrastructure.external.rest.BroadcastServiceClient;
import mystream.user.infrastructure.external.rest.ChannelServiceClient;

@TestConfiguration
public class ApiTestConfig {

  @Bean
  @Primary
  public BroadcastServiceClient broadcastServiceMock() {
    return new BroadcastServiceMock();
  }

  @Bean
  @Primary
  public ChannelServiceClient channelServiceClientMock() {
    return new ChannelServiceMock();
  }

}
