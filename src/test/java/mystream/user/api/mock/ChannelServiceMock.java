package mystream.user.api.mock;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelServiceMock {
  
  private Map<Long, Long> storeId = new HashMap<>();

  // @Override
  // public ApiResult<?> follow(Long id, FollowingDto followDto) {
  //   log.info("[TEST] called follow on mock channel service. {}", followDto);
  //   if (storeId.get(followDto.getChannelId()) == null) {
  //     return ApiResponse.error("not found channel", HttpStatus.BAD_REQUEST);
  //   }
  //   return ApiResponse.success(null);
  // }

  // @Override
  // public ApiResult<?> unfollow(Long id, FollowingDto followDto) {
  //   log.info("[TEST] called unfollow on mock channel service. {}", followDto);
  //   if (storeId.get(followDto.getChannelId()) == null) {
  //     return ApiResponse.error("not found channel", HttpStatus.BAD_REQUEST);
  //   }
  //   return ApiResponse.success(null);
  // }

  // public void addTestIds(Long userId, Long channgelId) {
  //   storeId.put(userId, channgelId);
  // }

}
