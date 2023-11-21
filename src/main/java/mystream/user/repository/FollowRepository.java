package mystream.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mystream.user.entity.FollowedChannel;

public interface FollowRepository extends JpaRepository<FollowedChannel, Long> {
  
}
