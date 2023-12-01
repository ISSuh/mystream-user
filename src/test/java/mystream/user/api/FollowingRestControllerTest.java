package mystream.user.api;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
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

import lombok.extern.slf4j.Slf4j;
import mystream.user.api.mock.ChannelServiceMock;
import mystream.user.domain.following.dto.FollowingDto;
import mystream.user.domain.following.entity.FollowStatus;
import mystream.user.domain.following.entity.FollowedChannel;
import mystream.user.domain.following.repository.FollowRepository;
import mystream.user.domain.user.entity.Email;
import mystream.user.domain.user.entity.User;
import mystream.user.domain.user.repository.UserRepository;

@SpringBootTest()
@AutoConfigureMockMvc
@Import(value = {ApiTestConfig.class})
// @AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@Slf4j
public class FollowingRestControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FollowRepository followRepository;

  @Autowired
  private ChannelServiceMock channelServiceMock;

  @BeforeEach
  public void beforeEach() {
    log.info("[TEST] beforeEach()");
    for (Long i = 0L ; i < 5 ; i++) {
      Long id = i + 1;
      Email email = Email.of("user" + id + "@gmail.com");
      String username = "user_" + id;
      String password = username;
      User user = new User(email, username, password);

      userRepository.saveEntity(user);

      Long channelId = id;
      channelServiceMock.addTestIds(id, channelId);
    }
  }

  @Test
  @DisplayName("follow test ")
  public void followTest() throws Exception {
    log.info("[TEST] followTest");

    // given
    Long userId = 1L;
    Long channelId = 2L;
    FollowingDto followingDto = new FollowingDto(userId, channelId);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(followingDto);

    // when
    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/following/follow")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(FollowingRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("followChannel"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty());

    Optional<FollowedChannel> channel = followRepository.findByUserId(userId);
    Assertions.assertThat(channel.isPresent()).isTrue();
    Assertions.assertThat(channel.get().getFollowStatus()).isEqualTo(FollowStatus.FOLLOW);
    Assertions.assertThat(channel.get().getChannelId()).isEqualTo(channelId);
  }

  @Test
  @DisplayName("not found user ")
  public void followNotfoundUserTest() throws Exception {
    log.info("[TEST] followNotfoundUserTest");

    // given
    Long userId = 100L;
    Long channelId = 2L;
    FollowingDto followingDto = new FollowingDto(userId, channelId);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(followingDto);

    // when
    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/following/follow")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().is4xxClientError())
      .andExpect(MockMvcResultMatchers.handler().handlerType(FollowingRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("followChannel"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(HttpStatus.NOT_FOUND.value()));

    Optional<FollowedChannel> channel = followRepository.findByUserId(userId);
    Assertions.assertThat(channel.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("not found channel")
  public void followNotfoundChannelTest() throws Exception {
    // given
    Long userId = 1L;
    Long channelId = 200L;
    FollowingDto followingDto = new FollowingDto(userId, channelId);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(followingDto);

    // when
    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/following/follow")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().is4xxClientError())
      .andExpect(MockMvcResultMatchers.handler().handlerType(FollowingRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("followChannel"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error.status").value(HttpStatus.BAD_REQUEST.value()));

    Optional<FollowedChannel> channel = followRepository.findByUserId(userId);
    Assertions.assertThat(channel.isEmpty()).isTrue();
  }

  @Test
  @DisplayName("unfollow test ")
  public void unfollowTest() throws Exception {
    // given
    Long userId = 2L;
    Long channelId = 3L;
    FollowingDto followingDto = new FollowingDto(userId, channelId);

    ObjectMapper mapper = new ObjectMapper();
    String dtoJsonString = mapper.writeValueAsString(followingDto);

    ResultActions result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/following/follow")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(FollowingRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("followChannel"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty());

    Optional<FollowedChannel> channel1 = followRepository.findByUserId(userId);
    Assertions.assertThat(channel1.isPresent()).isTrue();
    Assertions.assertThat(channel1.get().getFollowStatus()).isEqualTo(FollowStatus.FOLLOW);
    Assertions.assertThat(channel1.get().getChannelId()).isEqualTo(channelId);

    // when
    result = mockMvc.perform(
      MockMvcRequestBuilders.put("/api/user/v1/following/unfollow")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(dtoJsonString));

    // then
    result.andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.handler().handlerType(FollowingRestController.class))
      .andExpect(MockMvcResultMatchers.handler().methodName("unfollowChannel"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.error").isEmpty());

    Optional<FollowedChannel> channel2 = followRepository.findByUserId(userId);
    Assertions.assertThat(channel2.isPresent()).isTrue();
    Assertions.assertThat(channel2.get().getFollowStatus()).isEqualTo(FollowStatus.UNFOLLOW);
    Assertions.assertThat(channel2.get().getChannelId()).isEqualTo(channelId);
  }

}
