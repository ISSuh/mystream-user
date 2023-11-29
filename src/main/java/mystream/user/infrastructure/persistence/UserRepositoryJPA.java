package mystream.user.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mystream.user.domain.user.dto.UserDto;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;

public interface UserRepositoryJPA extends JpaRepository<User, Long>, UserRepository {
  
  Optional<User> findUserById(Long id);

  Optional<User> findUserByEmail(Email email);

  Optional<User> findUserByUsername(String username);

  @Query(
    "select new mystream.user.domain.user.dto.UserDto(u) " +
    "from User u " +
    "where u.id = :id")
  Optional<UserDto> findUserDtoById(Long id);


  default User saveEntity(User user) {
    return save(user);
  }

  default void deleteEntity(Long id) {
    deleteById(id);
  }

  default Optional<User> findById(Long id) {
    return findUserById(id);
  }

  default Optional<User> findByEmail(Email email) {
    return findUserByEmail(email);
  }

  default Optional<User> findByUsername(String username) {
    return findUserByUsername(username);
  }

  default Optional<UserDto> findUserDto(Long id) {
    return findUserDtoById(id);
  }

}
