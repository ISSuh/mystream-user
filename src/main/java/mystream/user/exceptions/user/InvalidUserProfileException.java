package mystream.user.exceptions.user;

public class InvalidUserProfileException extends RuntimeException {
  
  public InvalidUserProfileException(String message) {
    super(message);
  }

  public InvalidUserProfileException(String message, Throwable cause) {
    super(message, cause);
  }

}
