package mystream.user.infrastructure.external.rest.impl;

import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.domain.user.dto.NewStreamDto;

public class BroadcastServiceFeignClientFallback implements BroadcastServiceFeignClient {

  @Override
  public ApiResult<?> createStreamRequest(NewStreamDto newStreamDto) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
