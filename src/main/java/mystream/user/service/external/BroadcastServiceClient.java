package mystream.user.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mystream.user.dto.NewStreamDto;
import mystream.user.utils.ApiResponse;

@FeignClient("mystream-broadcast")
public interface BroadcastServiceClient {
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/broadcast/v1/streams/new")
  ApiResponse.ApiResult<?> createStream(NewStreamDto newStreamDto);

}
