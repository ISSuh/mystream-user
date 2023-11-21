package mystream.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user/v1/following")
public class FollowingController {
  
  @PostMapping("/{channelId}")
  void followChannel() {
  }

  @PutMapping("/{channelId}")
  void unFollowChannel() {
  }

}
