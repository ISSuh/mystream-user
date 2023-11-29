package mystream.user.domain.following.service;

import mystream.user.domain.following.dto.FollowingDto;
import mystream.user.domain.following.entity.FollowStatus;

public interface FollowService {

  void processFollowOrUnFollow(FollowingDto followDto, FollowStatus status);

}
