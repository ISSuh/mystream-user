package mystream.user.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import mystream.user.api.mock.BroadcastServiceMock;
import mystream.user.domain.user.dto.SignUpDto;
import mystream.user.domain.user.dto.UserDto;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Import(value = {TestConfig.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
@Slf4j
public class UserRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BroadcastServiceMock broadcastServiceMock;

  @BeforeEach
  public void BeforeEach() {
    for (Long i = 0L ; i < 10 ; i++) {
      Long id = i + 1;
      Email email = Email.of("user" + id + "@gmail.com");
      String username = "user_" + id;
      String password = username;
      User user = new User(email, username, password);

      userRepository.saveEntity(user);

      Long streamId = id;
      broadcastServiceMock.addTestIds(id, streamId);
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
  
  @Test
  @DisplayName("find user test")
  public void findUser() throws Exception {
    for (Long i = 0L ; i < 8 ; i++) {
      // given
      Long id = i + 1;

      // when
      ResultActions result = mockMvc.perform(
        MockMvcRequestBuilders.get("/api/user/v1/user/" + id)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON));
      
      // then
      String expectUsername = "user_" + id;
      String expectEmailAddress = "user" + id + "@gmail.com";
      result.andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
        .andExpect(MockMvcResultMatchers.handler().methodName("findById"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
        .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(expectUsername))
        .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(expectEmailAddress));

      }
  }

  @Test
  @DisplayName("update username")
  public void updateUserUsername() throws Exception {
    // given
    Long id = 1L;

    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));
    
    String expectUsername = "user_" + id;
    String expectEmailAddress = "user" + id + "@gmail.com";
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("findById"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(expectUsername))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(expectEmailAddress));

    // when
    String username = "change_" + id;
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setUsername(username);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/user/" + id + "/profile")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("updateUseProfile"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(username))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(expectEmailAddress));      
  }

  @Test
  @DisplayName("update user email")
  public void updateUserEmail() throws Exception {
    // given
    Long id = 2L;

    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));
    
    String expectUsername = "user_" + id;
    String expectEmailAddress = "user" + id + "@gmail.com";
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("findById"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(expectUsername))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(expectEmailAddress));

    // when
    String emailAddress = "change_" + id + "@gmail.com";
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setEmail(emailAddress);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/user/" + id + "/profile")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("updateUseProfile"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(expectUsername))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(emailAddress));      
  }

  @Test
  @DisplayName("update username and email")
  public void updateUsernameAndEmail() throws Exception {
    // given
    Long id = 3L;

    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.get("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));
    
    String expectUsername = "user_" + id;
    String expectEmailAddress = "user" + id + "@gmail.com";
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("findById"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(expectUsername))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(expectEmailAddress));

    // when
    String username = "change_" + id;
    String emailAddress = "change_" + id + "@gmail.com";
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setUsername(username);
    dto.setEmail(emailAddress);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/user/" + id + "/profile")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(UserRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("updateUseProfile"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty())
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(id))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(username))
      .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(emailAddress));      
  }
}
