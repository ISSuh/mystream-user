package mystream.user.service.external.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewStreamEvent {
  
  private Long id;

  private String username;

  public NewStreamEvent(Long id, String username) {
    this.id = id;
    this.username = username;
  }

}
