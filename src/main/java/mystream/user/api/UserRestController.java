package mystream.user.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mystream.user.api.utils.ApiResponse;
import mystream.user.domain.user.dto.SignUpDto;
import mystream.user.domain.user.dto.UserDto;
import mystream.user.domain.user.service.UserService;

@RestController
@RequestMapping("/api/user/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

  private final UserService userService;

  @PostMapping("/new")
  public ApiResponse.ApiResult<UserDto> create(
    @RequestBody SignUpDto signUpDto) {
    UserDto user = userService.create(signUpDto);
    return ApiResponse.success(user);
  }

  @GetMapping("/{id}")
  public ApiResponse.ApiResult<UserDto> findById(@PathVariable Long id) {
    UserDto user = userService.findUserById(id);
    return ApiResponse.success(user);
  }

  @PutMapping("/{id}/profile")
  public ApiResponse.ApiResult<UserDto> updateUseProfile(
    @PathVariable Long id,
    @RequestBody UserDto userDto) {
    log.error("[TEST] userdto = {}", userDto);
    UserDto user = userService.updateUseProfile(id, userDto);
    return ApiResponse.success(user);
  }

}
