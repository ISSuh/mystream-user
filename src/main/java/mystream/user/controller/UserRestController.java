package mystream.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mystream.user.dto.UserDto;
import mystream.user.utils.ApiResponse;

@RestController
@RequestMapping("/api/user/v1/user")
public class UserRestController {

  @PostMapping("/new")
  ApiResponse.ApiResult<?> create() {
    return ApiResponse.error(
      "error!", HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
