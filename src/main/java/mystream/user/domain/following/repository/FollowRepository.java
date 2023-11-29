package mystream.user.domain.following.repository;

import java.util.Optional;

import mystream.user.domain.following.entity.FollowedChannel;

public interface FollowRepository {
  
  FollowedChannel saveEntity(FollowedChannel followedChannel);

  void deleteEntity(Long id);

  Optional<FollowedChannel> findByChannelId(Long id);

  Optional<FollowedChannel> findByUserId(Long id);

}
