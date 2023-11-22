package mystream.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mystream.user.dto.UserDto;
import mystream.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
  Optional<User> findUserById(Long id);

  @Query(
    "select new package mystream.user.dto.UserDto(u) " +
    "from User u " +
    "where u.id = :id")
  Optional<UserDto> findUserDtoByIdTo(Long id);
}
