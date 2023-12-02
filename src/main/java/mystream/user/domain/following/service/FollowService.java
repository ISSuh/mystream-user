package mystream.user.domain.following.service;

import org.springframework.data.domain.Pageable;

import mystream.user.domain.following.dto.FollowingDto;
import mystream.user.domain.following.entity.FollowStatus;

public interface FollowService {

  void processFollowOrUnFollow(FollowingDto followDto, FollowStatus status);

  void findFollowingChannels(Long id, Pageable pageable);
}
