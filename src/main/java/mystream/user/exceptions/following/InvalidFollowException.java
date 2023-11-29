package mystream.user.exceptions.following;

public class InvalidFollowException extends RuntimeException {

  public InvalidFollowException(String message) {
    super(message);
  }

  public InvalidFollowException(String message, Throwable cause) {
    super(message, cause);
  }

}
