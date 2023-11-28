package mystream.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mystream.user.dto.FollowingDto;
import mystream.user.entity.FollowStatus;
import mystream.user.entity.FollowedChannel;
import mystream.user.entity.User;
import mystream.user.exceptions.InvalidFollowException;
import mystream.user.exceptions.NotFoundException;
import mystream.user.repository.FollowRepository;
import mystream.user.repository.UserRepository;
import mystream.user.service.external.ChannelServiceClient;
import mystream.user.utils.ApiResponse.ApiResult;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
  
  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final ChannelServiceClient channelServiceClient;


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

    FollowedChannel savedChannel = followRepository.save(channel);
    savedChannel.updateFollowStatus(status);
  }

  private FollowedChannel presentOrCtreateFollowedChannel(FollowingDto followDto) {
    Optional<User> findUser = userRepository.findUserById(followDto.getUserId());
    User user = findUser.orElseThrow(
      () -> new NotFoundException("not found user. " + followDto.getUserId()));

    Optional<FollowedChannel> findChannel = followRepository.findById(followDto.getUserId());
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
