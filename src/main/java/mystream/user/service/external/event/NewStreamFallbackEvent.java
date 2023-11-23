package mystream.user.service.external.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewStreamFallbackEvent {
  
  private Long id;

  public NewStreamFallbackEvent(Long id) {
    this.id = id;
  }

}
