package mystream.user.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import mystream.user.dto.SignUpDto;
import mystream.user.dto.UserDto;
import mystream.user.entity.Email;
import mystream.user.entity.User;
import mystream.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @BeforeAll
  public void beforeAll() {
    for (Long i = 0L ; i < 10 ; i++) {
      Long id = i + 1;
      Email email = Email.of("user" + id + "@gmail.com");
      String username = "user_" + id;
      String password = username;
      User user = new User(email, username, password);

      userRepository.save(user);
    }
  }

  @Test
  @DisplayName("find user test")
  @Order(1)
  public void findUser() throws Exception {
    for (Long i = 0L ; i < 8 ; i++) {
      Long id = i + 1;
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

      }
  }

  @Test
  @DisplayName("update username")
  @Order(2)
  public void updateUserUsername() throws Exception {
    Long id = 1L;

    // get original user
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

    // change username
    String username = "change_" + id;
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setUsername(username);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.patch("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

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
  @Order(3)
  public void updateUserEmail() throws Exception {
    Long id = 2L;

    // get original user
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

    // change username
    String emailAddress = "change_" + id + "@gmail.com";
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setEmail(emailAddress);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.patch("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

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
  @Order(4)
  public void updateUsernameAndEmail() throws Exception {
    Long id = 3L;

    // get original user
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

    // change username
    String username = "change_" + id;
    String emailAddress = "change_" + id + "@gmail.com";
    UserDto dto = new UserDto();
    dto.setId(id);
    dto.setUsername(username);
    dto.setEmail(emailAddress);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(dto);

    result = mockMvc.perform(
      MockMvcRequestBuilders.patch("/api/user/v1/user/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

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

  @Test
  @DisplayName("create new user")
  @Order(5)
  public void createUser() throws Exception {
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

    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.post("/api/user/v1/user/new")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

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
