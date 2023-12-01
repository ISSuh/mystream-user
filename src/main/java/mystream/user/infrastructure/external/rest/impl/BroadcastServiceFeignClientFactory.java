package mystream.user.infrastructure.external.rest.impl;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import mystream.user.infrastructure.external.rest.BroadcastServiceClient;

@Component
public class BroadcastServiceFeignClientFactory implements FallbackFactory<BroadcastServiceClient> {

  @Override
  public BroadcastServiceClient create(Throwable cause) {  
    return new BroadcastServiceFeignClientFallback() {
      
    };
  }
  
}
