package mystream.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mystream.user.domain.following.entity.FollowedChannel;
import mystream.user.domain.following.repository.FollowRepository;

public interface FollowRepositoryJPA extends JpaRepository<FollowedChannel, Long>, FollowRepository {

  Optional<FollowedChannel> findFollowedChannelByChannelId(Long id);

  @Query(
    "select fc " +
    "from FollowedChannel fc " +
    "join fetch fc.user u " +
    "where u.id = :id"
  )
  Optional<FollowedChannel> findFollowedChannelByUserId(Long id);

  default FollowedChannel saveEntity(FollowedChannel followedChannel) {
    return save(followedChannel);
  }

  default void deleteEntity(Long id) {
    deleteById(id);
  }

  default Optional<FollowedChannel> findByChannelId(Long id) {
    return findFollowedChannelByChannelId(id);
  }

  default Optional<FollowedChannel> findByUserId(Long id) {
    return findFollowedChannelByUserId(id);
  }

}
