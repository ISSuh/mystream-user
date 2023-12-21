package mystream.user.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import mystream.user.domain.user.dto.SignUpDto;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;

@SpringBootTest()
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class TempUserRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void beforeEach() {
    for (Long i = 0L ; i < 10 ; i++) {
      Long id = i + 1;
      Email email = Email.of("user" + id + "@gmail.com");
      String username = "user_" + id;
      String password = username;
      User user = new User(email, username, password);

      userRepository.saveEntity(user);
    }
  }

  @Test
  @DisplayName("create new user")
  public void createUser() throws Exception {
    // given
    Long id = 11L;
    String username = "user_" + id;
    String emailAddress = "user_" + id + "@gmail.com";
    String password = username;

    SignUpDto sigeDto = new SignUpDto();
    sigeDto.setUsername(username);
    sigeDto.setEmail(emailAddress);
    sigeDto.setPassword(password);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(sigeDto);

    // when
    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.post("/api/user/v1/user/new")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("create"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(username))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(emailAddress));      
  }

}
