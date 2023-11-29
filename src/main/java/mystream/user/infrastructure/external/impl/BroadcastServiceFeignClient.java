package mystream.user.infrastructure.external.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.user.dto.NewStreamDto;

@FeignClient(name = "mystream-broadcast", primary = false)
public interface BroadcastServiceFeignClient {
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/broadcast/v1/streams/new")
  ApiResponse.ApiResult<?> createStreamRequest(NewStreamDto newStreamDto);

  default ApiResponse.ApiResult<?> createStream(NewStreamDto newStreamDto) {
    return createStreamRequest(newStreamDto);
  }

}
