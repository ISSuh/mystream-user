package mystream.user.domain.user.repository;

import java.util.Optional;

import mystream.user.domain.user.dto.UserDto;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;

public interface UserRepository {

  User saveEntity(User user);

  void deleteEntity(Long id);

  Optional<User> findById(Long id);

  Optional<User> findByEmail(Email email);

  Optional<User> findByUsername(String username);

  Optional<UserDto> findUserDto(Long id);

}
