package mystream.user.dto;

import lombok.Data;

@Data
public class FollowDto {
  
  private Long userId;

  private Long channelId;

  public FollowDto() {
  }

  public FollowDto(Long userId, Long channelId) {
    this.userId = userId;
    this.channelId = channelId;
  }

}
