package mystream.user.infrastructure.external;

import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.user.dto.NewStreamDto;

public interface BroadcastServiceClient {
  
  ApiResponse.ApiResult<?> createStream(NewStreamDto newStreamDto);

}
