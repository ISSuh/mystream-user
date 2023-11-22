package mystream.user.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mystream.user.dto.FollowDto;
import mystream.user.entity.FollowStatus;
import mystream.user.service.FollowService;
import mystream.user.utils.ApiResponse;

@RestController
@RequestMapping("api/user/v1/following")
@RequiredArgsConstructor
public class FollowingRestController {
  
  private final FollowService followService;

  @PostMapping("/follow")
  public ApiResponse.ApiResult<?> followChannel(@RequestBody FollowDto followDto) {
    followService.processFollowOrUnFollow(followDto, FollowStatus.FOLLOW);
    return ApiResponse.success(null);
  }

  @PatchMapping("/unfollow")
  public ApiResponse.ApiResult<?> unFollowChannel(@RequestBody FollowDto followDto) {
    followService.processFollowOrUnFollow(followDto, FollowStatus.UNFOLLOW);
    return ApiResponse.success(null);
  }

}
