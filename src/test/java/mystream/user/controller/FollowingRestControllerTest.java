package mystream.user.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import mystream.user.entity.Email;
import mystream.user.entity.FollowedChannel;
import mystream.user.entity.User;
import mystream.user.repository.FollowRepository;
import mystream.user.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FollowingRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FollowRepository followRepository;

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

    for (Long i = 1L ; i < 10 ; i *= 2) {
      User user = userRepository.findById(i).get();
      FollowedChannel channel = new FollowedChannel(i, user);
      followRepository.save(channel);
    }
  }
}
