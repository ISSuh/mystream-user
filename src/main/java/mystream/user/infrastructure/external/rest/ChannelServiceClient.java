package mystream.user.infrastructure.external.rest;

import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.following.dto.FollowingDto;

public interface ChannelServiceClient {

  ApiResponse.ApiResult<?> follow(Long id, FollowingDto followDto);

  ApiResponse.ApiResult<?> unfollow(Long id, FollowingDto followDto);

}
