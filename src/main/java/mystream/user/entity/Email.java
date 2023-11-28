package mystream.user.entity;

import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Email {
  
  @Column(name = "email")
  private String address;

  protected Email() {
  }

  public Email(String address) {
    Preconditions.checkArgument(StringUtils.hasText(address), "email address must be not empty");
    Preconditions.checkArgument(isValid(address), "email address is not valid");

    this.address = address;
  }

  public static Email of(String email) {
    return new Email(email);
  }

  public static boolean isValid(String email) {
    return Pattern.matches("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+", email);
  }

  public String toEmailAddress() {
    return this.address;
  }

  public boolean isEqualAddress(String address) {
    return (this.address == address);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
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
    if (address == null) {
      if (other.address != null)
        return false;
    } else if (!address.equals(other.address))
      return false;
    return true;
  }
  
}
