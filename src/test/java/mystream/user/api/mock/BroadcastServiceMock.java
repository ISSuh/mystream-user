package mystream.user.api.mock;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import mystream.user.api.utils.ApiResponse;
import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.domain.user.dto.NewStreamDto;
import mystream.user.infrastructure.external.BroadcastServiceClient;

@Slf4j
public class BroadcastServiceMock implements BroadcastServiceClient {

  private Map<Long, Long> storeId = new HashMap<>();

  @Override
  public ApiResult<?> createStream(NewStreamDto newStreamDto) {
    log.info("[TEST] called createStream on mock broadcast service. {}", newStreamDto);
    return ApiResponse.success(null);
  }
  
  public void addTestIds(Long userId, Long streamId) {
    storeId.put(userId, streamId);
  }

}
