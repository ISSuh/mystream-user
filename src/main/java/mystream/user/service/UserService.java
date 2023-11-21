package mystream.user.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mystream.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;

}
