package raisetech.StudentManagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Getter;

/**
 * REST API のカスタムしたエラーレスポンスを表すクラス。
 */
@Schema(description = "エラーレスポンス")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  @Schema(description = "HTTP ステータスコード", example = "400")
  private int status;

  @Schema(description = "エラーコード", example = "VALIDATION_ERROR")
  private String errorCode;

  @Schema(description = "エラーメッセージ", example = "入力のデータが不正です。")
  private String error;

  @Schema(description = "エラー詳細情報（バリデーションエラー時のみ）",
      example = "{\"field1\": \"値は必須です。\", \"field2\": \"文字数は100字以内にする必要があります。\"}")
  private Map<String, String> message;

  /**
   * シンプルなエラーレスポンス（詳細エラーメッセージなし）を作成するファクトリメソッド。
   *
   * @param status    HTTP ステータスコード
   * @param errorCode エラーコード
   * @param error     エラーメッセージ
   */
  public ErrorResponse(int status, String errorCode, String error) {
    this.status = status;
    this.errorCode = errorCode;
    this.error = error;
  }

  /**
   * エラーレスポンスのコンストラクタ。
   *
   * @param status    HTTP ステータスコード
   * @param errorCode エラーコード
   * @param error     エラーメッセージ
   * @param message   フィールドごとの詳細エラーメッセージ（例: {"studentId": "受講生IDは必須です。" }）
   */
  public ErrorResponse(int status, String errorCode, String error, Map<String, String> message) {
    this.status = status;
    this.errorCode = errorCode;
    this.error = error;
    this.message = message;
  }

}
