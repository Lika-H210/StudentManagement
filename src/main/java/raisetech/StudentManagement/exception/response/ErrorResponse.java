package raisetech.StudentManagement.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * バリデーションエラーやアプリケーション例外が発生した際に、 APIのレスポンスとして返されるエラー情報を表すクラスです。
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"errorCode", "errorStatus", "message", "fieldErrorMessages"})
public class ErrorResponse {

  private int errorCode;
  private HttpStatus errorStatus;
  private String message;
  private Map<String, List<String>> fieldErrorMessages;

  public ErrorResponse(HttpStatus status, Map<String, List<String>> fieldErrorMessages) {
    this.errorCode = status.value();
    this.errorStatus = status;
    this.fieldErrorMessages = fieldErrorMessages;
  }

  public ErrorResponse(HttpStatus status, String message) {
    this.errorCode = status.value();
    this.errorStatus = status;
    this.message = message;
  }

}
