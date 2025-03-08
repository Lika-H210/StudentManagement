package raisetech.StudentManagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Getter;

/**
 * REST API のカスタムしたエラーレスポンスを表すクラス。
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  /**
   * HTTP ステータスコード
   */
  private int status;

  /**
   * エラーコード（API 内で識別可能なコード）
   */
  private String errorCode;

  /**
   * エラーメッセージ（概要）
   */
  private String error;

  /**
   * 詳細なエラーメッセージ（バリデーションエラー時のフィールドごとのメッセージ）
   */
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
