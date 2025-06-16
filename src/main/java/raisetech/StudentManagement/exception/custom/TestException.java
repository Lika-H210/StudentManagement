package raisetech.StudentManagement.exception.custom;

public class TestException extends Exception {

  public TestException() {
    super();
  }

  public TestException(String message) {
    super(message);
  }

  public TestException(String message, Throwable cause) {
    super(message, cause);
  }
}
