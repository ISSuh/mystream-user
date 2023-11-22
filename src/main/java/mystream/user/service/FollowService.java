package mystream.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mystream.user.dto.FollowDto;
import mystream.user.entity.FollowStatus;
import mystream.user.entity.FollowedChannel;
import mystream.user.entity.User;
import mystream.user.exceptions.InvalidFollowException;
import mystream.user.exceptions.NotFoundException;
import mystream.user.repository.FollowRepository;
import mystream.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
  
  private final UserRepository userRepository;
  private final FollowRepository followRepository;

  public void processFollowOrUnFollow(FollowDto followDto, FollowStatus status) {
    FollowedChannel channel = presentOrCtreateFollowedChannel(followDto);
    if (channel.getFollowStatus() == status) {
      throw new InvalidFollowException("already " + status.toString() + " channel. " + followDto.getChannelId());
    }

    channel.updateFollowStatus(status);
  }

  private FollowedChannel presentOrCtreateFollowedChannel(FollowDto followDto) {
    Optional<User> findUser = userRepository.findUserById(followDto.getUserId());
    User user = findUser.orElseThrow(
      () -> new NotFoundException("not found user. " + followDto.getUserId()));

    Optional<FollowedChannel> findChannel = followRepository.findById(followDto.getUserId());
    return findChannel.orElse(
        new FollowedChannel(followDto.getChannelId(), user));
  }

}
