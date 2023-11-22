package mystream.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mystream.user.dto.UserDto;
import mystream.user.entity.Email;
import mystream.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
  Optional<User> findUserById(Long id);

  Optional<User> findUserByEmail(Email email);

  Optional<User> findUserByUsername(String username);

  @Query(
    "select new mystream.user.dto.UserDto(u) " +
    "from User u " +
    "where u.id = :id")
  Optional<UserDto> findUserDtoByIdTo(Long id);


}
