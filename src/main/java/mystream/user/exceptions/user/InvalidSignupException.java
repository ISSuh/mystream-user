package mystream.user.exceptions.user;

public class InvalidSignupException extends RuntimeException {
  
  public InvalidSignupException(String message) {
    super(message);
  }

  public InvalidSignupException(String message, Throwable cause) {
    super(message, cause);
  }

}
