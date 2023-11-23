package mystream.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewStreamDto {
  
  private Long id;
  private String username;
  
  public NewStreamDto(Long id, String username) {
    this.id = id;
    this.username = username;
  }

}
