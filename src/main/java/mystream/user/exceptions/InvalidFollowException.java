package mystream.user.exceptions;

public class InvalidFollowException extends RuntimeException {

  public InvalidFollowException(String message) {
    super(message);
  }

  public InvalidFollowException(String message, Throwable cause) {
    super(message, cause);
  }

}
