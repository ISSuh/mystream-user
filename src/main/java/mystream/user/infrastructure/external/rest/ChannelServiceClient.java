package mystream.user.infrastructure.external.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.following.dto.FollowingDto;

@FeignClient("mystream-channel")
public interface ChannelServiceClient {

  @RequestMapping(method = RequestMethod.PUT, value = "/api/channel/v1/channel/{id}/follow")
  ApiResponse.ApiResult<?> followRequest(
    @PathVariable Long id,
    @RequestBody FollowingDto followDto);

  @RequestMapping(method = RequestMethod.PUT, value = "/api/channel/v1/channel/{id}/unfollow")
  ApiResponse.ApiResult<?> unfollowRequest(
    @PathVariable Long id,
    @RequestBody FollowingDto followDto);

  default ApiResponse.ApiResult<?> follow(Long id, FollowingDto followDto) {
    return followRequest(id, followDto);
  }

  default ApiResponse.ApiResult<?> unfollow(Long id, FollowingDto followDto) {
    return unfollowRequest(id, followDto);
  }

}
