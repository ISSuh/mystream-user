package mystream.user.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import mystream.user.api.utils.ApiResponse;
import mystream.user.api.utils.ApiResponse.ApiResult;
import mystream.user.exceptions.common.NotFoundException;
import mystream.user.exceptions.following.InvalidFollowException;
import mystream.user.exceptions.user.InvalidSignupException;
import mystream.user.exceptions.user.InvalidUserProfileException;

@ControllerAdvice
public class GeneralExceptionHandler {
  
  private ResponseEntity<ApiResponse.ApiResult<?>> response(Throwable throwable, HttpStatus status) {
    return response(throwable.getMessage(), status);
  }

  private ResponseEntity<ApiResponse.ApiResult<?>> response(String message, HttpStatus status) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("content-type", MediaType.APPLICATION_JSON.toString());

    ApiResult<?> errorResponse = ApiResponse.error(message, status);
    return new ResponseEntity<>(errorResponse, headers, status);
  }

  @ExceptionHandler({
    NotFoundException.class,
    NoHandlerFoundException.class
  })
  public ResponseEntity<?> handleNotFound(Exception e) {
    return response(e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    IllegalStateException.class,
    InvalidFollowException.class,
    InvalidSignupException.class,
    InvalidUserProfileException.class
  })
  public ResponseEntity<?> handleBadRequest(Exception e) {
    return response(e, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<?> handleMethodNottSupported(Exception e) {
    return response(e, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler({
    Exception.class,
    RuntimeException.class
  })
  public ResponseEntity<?> handleOtherException(Exception e) {
    return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
