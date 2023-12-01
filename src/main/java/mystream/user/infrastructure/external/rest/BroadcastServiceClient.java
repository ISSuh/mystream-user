package mystream.user.infrastructure.external.rest;

import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.user.dto.NewStreamDto;

public interface BroadcastServiceClient {
  
  ApiResponse.ApiResult<?> createStream(NewStreamDto newStreamDto);

}
