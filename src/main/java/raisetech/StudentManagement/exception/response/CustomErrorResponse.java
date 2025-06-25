package raisetech.StudentManagement.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * バリデーションエラーやアプリケーション例外が発生した際に、 APIのレスポンスとして返されるエラー情報を表すクラスです。
 */
@Schema(description = "エラーレスポンス")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"errorCode", "errorStatus", "message", "fieldErrorMessages"})
public class CustomErrorResponse {

  @Schema(description = "エラーコード(例：400,404など)", example = "400")
  private int errorCode;

  @Schema(description = "エラーステータス(例：BAD_REQUEST, NOT_FOUND など)", example = "BAD_REQUEST")
  private HttpStatus errorStatus;

  @Schema(description = "一般的なエラーメッセージ。(fieldErrorMessagesとは排他)",
      example = "受講生情報の取得中に問題が発生しました。")
  private String message;

  @Schema(
      description = "フィールド毎のエラー内容。キーは対象フィールド名、値はそのフィールドに関連するエラーメッセージの配列。(messageとは排他)",
      example = """
          {
            "student.fullName": ["名前は必須です", "名前は50文字以内で入力してください"],
            "student.email": ["メールアドレスの形式が不正です"]
          }
          """
  )
  private Map<String, List<String>> fieldErrorMessages;

  public CustomErrorResponse(HttpStatus status, Map<String, List<String>> fieldErrorMessages) {
    this.errorCode = status.value();
    this.errorStatus = status;
    this.fieldErrorMessages = fieldErrorMessages;
  }

  public CustomErrorResponse(HttpStatus status, String message) {
    this.errorCode = status.value();
    this.errorStatus = status;
    this.message = message;
  }

}
