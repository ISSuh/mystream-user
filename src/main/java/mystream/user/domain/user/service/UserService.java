package mystream.user.domain.user.service;

import mystream.user.domain.user.dto.SignUpDto;
import mystream.user.domain.user.dto.UserDto;

public interface UserService {
  
  public UserDto findUserById(Long id);

  public void delete(Long id);

  UserDto create(SignUpDto signUpDto);

  UserDto updateUseProfile(Long id, UserDto userDto);

}
