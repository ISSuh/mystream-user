package mystream.user.infrastructure.external.rest;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BroadcastServiceFeignClientFactory implements FallbackFactory<BroadcastServiceClient> {

  @Override
  public BroadcastServiceClient create(Throwable cause) {  
    return new BroadcastServiceFeignClientFallback() {
      
    };
  }
  
}
