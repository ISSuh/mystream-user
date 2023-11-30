package mystream.user.infrastructure.external.rest;

import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.domain.user.dto.NewStreamDto;

public class BroadcastServiceFeignClientFallback implements BroadcastServiceClient {

  @Override
  public ApiResult<?> createStreamRequest(NewStreamDto newStreamDto) {
    // TODO Auto-generated method stub
    return null;
  }
  
}
