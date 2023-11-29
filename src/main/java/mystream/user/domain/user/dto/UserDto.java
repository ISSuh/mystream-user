package mystream.user.domain.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mystream.user.domain.user.entity.User;

@Data
@NoArgsConstructor
public class UserDto {
  
  private Long id;

  private String email;

  private String username;

  public UserDto(User user) {
    this.id = user.getId();
    this.email = user.getEmail().toEmailAddress();
    this.username = user.getUsername();
  }

}
