package mystream.user.dto;

import lombok.Data;

@Data
public class SignUpDto {

  private String email;

  private String username;

  private String password;

  public SignUpDto() {
  }

  public SignUpDto(String email, String username, String password) {
    this.email = email;
    this.username = username;
    this.password = password;
  }

}
