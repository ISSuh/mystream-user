package mystream.user.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mystream.user.dto.SignUpDto;
import mystream.user.dto.UserDto;
import mystream.user.entity.Email;
import mystream.user.entity.User;
import mystream.user.exceptions.InvalidSignupException;
import mystream.user.exceptions.InvalidUserProfileException;
import mystream.user.exceptions.NotFoundException;
import mystream.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserDto findUserById(Long id) {
    return userRepository.findUserDtoByIdTo(id)
      .orElseThrow(() -> new NotFoundException("not found user. " + id));
  }

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

    // TODO(issuh) : need send user create event to broadcast, channel service

    userRepository.save(user);

    return new UserDto(user);
  }

  public UserDto updateUseProfile(Long id, UserDto userDto) {
    User user =
      userRepository.findUserById(id)
        .orElseThrow(() -> new NotFoundException("not found user. " + id));
    
    if ((userDto.getUsername() != null) &&
        (user.getUsername() != userDto.getUsername())) {
      updateUsername(user, userDto.getUsername());
    }

    if ((userDto.getEmail() != null) &&
        (!user.getEmail().isEqualAddress(userDto.getEmail()))) {
      updateEmail(user, userDto.getEmail());
    }

    User result = userRepository.save(user);
    return new UserDto(result);
  }

  private boolean isValidEmail(String emailAddress) {
    return Email.isValid(emailAddress);
  }

  private boolean isEmailExsit(Email email) {
    Optional<User> user = userRepository.findUserByEmail(email);
    return user.isPresent();
  }

  private boolean isUserNameExist(String username) {
    Optional<User> user = userRepository.findUserByUsername(username);
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
