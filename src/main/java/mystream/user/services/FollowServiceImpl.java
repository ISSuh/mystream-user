package mystream.user.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.domain.following.dto.FollowingDto;
import mystream.user.domain.following.entity.FollowStatus;
import mystream.user.domain.following.entity.FollowedChannel;
import mystream.user.domain.following.repository.FollowRepository;
import mystream.user.domain.following.service.FollowService;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;
import mystream.user.exceptions.common.NotFoundException;
import mystream.user.exceptions.following.InvalidFollowException;
import mystream.user.infrastructure.external.rest.ChannelServiceClient;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
  
  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  private final ChannelServiceClient channelServiceClient;

  @Override
  public void processFollowOrUnFollow(FollowingDto followDto, FollowStatus status) {
    FollowedChannel channel = presentOrCtreateFollowedChannel(followDto);
    if (channel.getFollowStatus() == status) {
      throw new InvalidFollowException("already " + status.toString() + " channel. " + followDto.getChannelId());
    }

    // request to channel service
    if (status == FollowStatus.FOLLOW) {
      requestUpdateToFollow(followDto);
    } else {
      requestUpdateToUnfollow(followDto);
    }

    FollowedChannel savedChannel = followRepository.saveEntity(channel);
    savedChannel.updateFollowStatus(status);
  }

  @Override
  public void findFollowingChannels(Long id, Pageable pageable) {
    return;
  }

  private FollowedChannel presentOrCtreateFollowedChannel(FollowingDto followDto) {
    Optional<User> findUser = userRepository.findById(followDto.getUserId());
    User user = findUser.orElseThrow(
      () -> new NotFoundException("not found user. " + followDto.getUserId()));

    Optional<FollowedChannel> findChannel = followRepository.findByChannelId(followDto.getChannelId());
    return findChannel.orElse(
        new FollowedChannel(followDto.getChannelId(), user));
  }

  private void requestUpdateToFollow(FollowingDto followDto) {
    ApiResult<?> response = channelServiceClient.follow(followDto.getChannelId(), followDto);
    if (!response.isSuccess()) {
      throw new InvalidFollowException("can not update follow status from channel service");
    }
  }

  private void requestUpdateToUnfollow(FollowingDto followDto) {
    ApiResult<?> response = channelServiceClient.unfollow(followDto.getChannelId(), followDto);
    if (!response.isSuccess()) {
      throw new InvalidFollowException("can not update unfollow status from channel service");
    }
  }

}
