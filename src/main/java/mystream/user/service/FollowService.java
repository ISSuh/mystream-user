package mystream.user.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mystream.user.repository.FollowRepository;

@Service
@RequiredArgsConstructor
public class FollowService {
  
  private final FollowRepository followRepository;

  
}
