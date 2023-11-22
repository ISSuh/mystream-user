package mystream.user.entity;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Email {
  
  private String email;

  protected Email() {
  }

  public Email(String email) {
    Preconditions.checkArgument(!StringUtils.hasText(email), "email must be not empty");
    Preconditions.checkArgument(isValid(email), "email is not valid");

    this.email = email;
  }

  public static Email of(String email) {
    return new Email(email);
  }

  public static boolean isValid(String email) {
    return Pattern.matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", email);
  }

  public String toEmailAddress() {
    return this.email;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Email other = (Email) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    return true;
  }

  
}
