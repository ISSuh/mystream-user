package mystream.user.infrastructure.external.rest.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.user.dto.NewStreamDto;
import mystream.user.infrastructure.external.rest.BroadcastServiceClient;

@FeignClient(
  name = "mystream-broadcast",
  primary = false
)
public interface BroadcastServiceFeignClient extends BroadcastServiceClient {
  
  @RequestMapping(
    method = RequestMethod.POST,
    value = "/api/broadcast/v1/streams/new")
  @CircuitBreaker(name = "myCircuitBreaker", fallbackMethod = "fallback")
  ApiResponse.ApiResult<?> createStreamRequest(NewStreamDto newStreamDto);

  default ApiResponse.ApiResult<?> createStream(NewStreamDto newStreamDto) {
    return createStreamRequest(newStreamDto);
  }

  default void fallback(Throwable e) {
    throw new RuntimeException("fallback createStreamRequest");
  }

}
