package raisetech.StudentManagement.exception.custom;

import lombok.Getter;

@Getter
public class NotUniqueException extends RuntimeException {

  public NotUniqueException(String message) {
    super(message);
  }
}
