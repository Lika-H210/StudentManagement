package raisetech.StudentManagement.exception.custom;

import lombok.Getter;

@Getter
public class NotUniqueException extends Exception {

  public NotUniqueException(String message) {
    super(message);
  }
}
