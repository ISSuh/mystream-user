package mystream.user.api.utils;

import org.springframework.http.HttpStatus;

public class ApiResponse {
  
  public static <T> ApiResult<T> success(T response) {
    return new ApiResult<>(true, response, null);
  }

  public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
    return error(throwable.getMessage(), status);
  }

  public static ApiResult<?> error(String message, HttpStatus status) {
    return new ApiResult<>(false, null, new Error(message, status.value()));
  }

  public static class Error {
    private String message;
    private int status;

    public Error(String message, int status) {
      this.message = message;
      this.status = status;
    }

    public String getMessage() {
      return message;
    }

    public int getstatus() {
      return status;
    }

    @Override
    public String toString() {
      return "Error [message=" + message + ", status=" + status + "]";
    }
  }

  public static class ApiResult<T> {
    private boolean success;
    private T result;
    private Error error;
    
    public boolean isSuccess() {
      return success;
    }
    public T getResult() {
      return result;
    }
    public Error getError() {
      return error;
    }

    public ApiResult(boolean success, T result, Error error) {
      this.success = success;
      this.result = result;
      this.error = error;
    }

    @Override
    public String toString() {
      return "ApiResult [success=" + success + ", result=" + result + ", error=" + error + "]";
    }
  }
}
