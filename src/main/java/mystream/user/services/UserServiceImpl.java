package mystream.user.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.domain.user.dto.NewStreamDto;
import mystream.user.domain.user.dto.SignUpDto;
import mystream.user.domain.user.dto.UserDto;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;
import mystream.user.domain.user.service.UserService;
import mystream.user.exceptions.common.NotFoundException;
import mystream.user.exceptions.user.InvalidSignupException;
import mystream.user.exceptions.user.InvalidUserProfileException;
import mystream.user.infrastructure.external.rest.BroadcastServiceClient;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private final BroadcastServiceClient broadcastServiceClient;

  @Override
  public UserDto findUserById(Long id) {
    return userRepository.findUserDto(id)
      .orElseThrow(() -> new NotFoundException("not found user. " + id));
  }

  @Override
  public void delete(Long id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      userRepository.deleteEntity(user.get().getId());
    }
  }

  @Override
  public UserDto create(SignUpDto signUpDto) {
    if (!isValidEmail(signUpDto.getEmail())) {
      throw new InvalidSignupException("invalid email address");
    }

    Email email = Email.of(signUpDto.getEmail());
    if (isEmailExsit(email)) {
      throw new InvalidSignupException("email address already exist");
    }

    if (isUserNameExist(signUpDto.getUsername())) {
      throw new InvalidSignupException("username already exist");
    }

    String encodePassword = passwordEncoder.encode(signUpDto.getPassword());
    User user = new User(email, signUpDto.getUsername(), encodePassword);

    User saved = userRepository.saveEntity(user);
    
    // reqquest create new stream
    NewStreamDto newStreamDto = new NewStreamDto(saved.getId(), saved.getUsername());
    ApiResult<?> result = broadcastServiceClient.createStream(newStreamDto);
    if (!result.isSuccess()) {
      userRepository.deleteEntity(saved.getId());
      throw new InvalidSignupException("stream create fail");
    }

    return new UserDto(saved);
  }

  @Override
  public UserDto updateUseProfile(Long id, UserDto userDto) {
    User user =
      userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("not found user. " + id));
    
    if ((userDto.getUsername() != null) &&
        (user.getUsername() != userDto.getUsername())) {
      updateUsername(user, userDto.getUsername());
    }

    if ((userDto.getEmail() != null) &&
        (!user.getEmail().isEqualAddress(userDto.getEmail()))) {
      updateEmail(user, userDto.getEmail());
    }

    User result = userRepository.saveEntity(user);
    return new UserDto(result);
  }

  private boolean isValidEmail(String emailAddress) {
    return Email.isValid(emailAddress);
  }

  private boolean isEmailExsit(Email email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user.isPresent();
  }

  private boolean isUserNameExist(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    return user.isPresent();
  }

  private void updateUsername(User user, String username) {
    if (isUserNameExist(username)) {
      throw new InvalidUserProfileException("alread exist username");
    }
    user.updateUserName(username);
  }

  private void updateEmail(User user, String emailAddress) {
    if (!isValidEmail(emailAddress)) {
      throw new InvalidUserProfileException("invalid email address");
    }

    Email email = Email.of(emailAddress);
    if (isEmailExsit(email)) {
      throw new InvalidUserProfileException("alread exist email address");
    }

    user.updateEmail(email);
  }
}
