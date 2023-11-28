package mystream.user.dto;

import lombok.Data;
import mystream.user.entity.User;

@Data
public class UserDto {
  
  private Long id;

  private String email;

  private String username;

  public UserDto() {
  }

  public UserDto(User user) {
    this.id = user.getId();
    this.email = user.getEmail().toEmailAddress();
    this.username = user.getUsername();
  }

}
