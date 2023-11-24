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


  // @Test
  // @Order(2)
  // public void createUserFallback() throws JsonMappingException, JsonProcessingException {
  //   String email = "tester2@gmail.com";
  //   String usernmae = "tester2";
  //   String password = "tester2";

  //   SignUpDto dto = new SignUpDto(email, usernmae, password);
  //   UserDto user = userService.create(dto);

  //   testProducer.sendStreamFallback(user.getId());

  //   ArgumentCaptor<String> cap = ArgumentCaptor.forClass(String.class);
  //   Mockito.verify(eventFallbackService, timeout(3000).times(1))
  //     .createStreamFallback(cap.capture());

  //   ObjectMapper mapper = new ObjectMapper();
  //   NewStreamFacllbackEvent event = mapper.readValue(cap.getValue(), NewStreamFacllbackEvent.class);
  //   Assertions.assertThat(event.getId()).isEqualTo(user.getId());
    
  //   Assertions.assertThatThrownBy(() -> userService.findUserById(user.getId()))
  //     .isInstanceOf(NotFoundException.class);
  // }