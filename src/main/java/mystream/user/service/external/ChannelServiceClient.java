package mystream.user.service.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mystream.user.dto.FollowingDto;
import mystream.user.dto.NewChannelDto;
import mystream.user.utils.ApiResponse;

@FeignClient("mystream-channel")
public interface ChannelServiceClient {
  
  @RequestMapping(method = RequestMethod.POST, value = "/api/channel/v1/channel/new")
  ApiResponse.ApiResult<?> create(NewChannelDto newChannelDto);

  @RequestMapping(method = RequestMethod.PUT, value = "/api/channel/v1/channel/{id}/follow")
  ApiResponse.ApiResult<?> follow(Long id, FollowingDto followDto);

  @RequestMapping(method = RequestMethod.PUT, value = "/api/channel/v1/channel/{id}/unfollow")
  ApiResponse.ApiResult<?> unfollow(Long id, FollowingDto followDto);
}
