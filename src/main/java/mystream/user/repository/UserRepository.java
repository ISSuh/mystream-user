package mystream.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mystream.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  
}
