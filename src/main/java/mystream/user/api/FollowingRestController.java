package mystream.user.api;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.following.dto.FollowingDto;
import mystream.user.domain.following.entity.FollowStatus;
import mystream.user.domain.following.service.FollowService;

@RestController
@RequestMapping("/api/user/v1/following")
@RequiredArgsConstructor
public class FollowingRestController {
  
  private final FollowService followService;

  @PutMapping("/follow")
  public ApiResponse.ApiResult<?> followChannel(@RequestBody FollowingDto followDto) {
    followService.processFollowOrUnFollow(followDto, FollowStatus.FOLLOW);
    return ApiResponse.success(null);
  }

  @PutMapping("/unfollow")
  public ApiResponse.ApiResult<?> unfollowChannel(@RequestBody FollowingDto followDto) {
    followService.processFollowOrUnFollow(followDto, FollowStatus.UNFOLLOW);
    return ApiResponse.success(null);
  }

}
