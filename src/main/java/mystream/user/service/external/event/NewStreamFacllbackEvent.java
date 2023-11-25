package mystream.user.service.external.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewStreamFacllbackEvent {
  
  private Long id;

  public NewStreamFacllbackEvent(Long id) {
    this.id = id;
  }

}
