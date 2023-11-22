package mystream.user.dto;

import lombok.Data;

@Data
public class SignUpDto {

  private String email;

  private String username;

  private String passwd;

  public SignUpDto() {
  }

  public SignUpDto(String email, String username, String passwd) {
    this.email = email;
    this.username = username;
    this.passwd = passwd;
  }

}
