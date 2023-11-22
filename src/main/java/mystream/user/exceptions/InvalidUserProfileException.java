package mystream.user.exceptions;

public class InvalidUserProfileException extends RuntimeException {
  
  public InvalidUserProfileException(String message) {
    super(message);
  }

  public InvalidUserProfileException(String message, Throwable cause) {
    super(message, cause);
  }

}
