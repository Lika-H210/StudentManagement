package raisetech.StudentManagement.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST API の例外を一元管理するハンドラークラス。
 */
@ControllerAdvice
public class RestExceptionHandler {

  /**
   * カスタム例外（{@code TestException}）を処理する。
   *
   * @param ex 発生した {@code TestException}
   * @return HTTP 404 NOT FOUND のエラーレスポンス
   */
  @ExceptionHandler(TestException.class)
  public ResponseEntity<ErrorResponse> handleTestException(TestException ex) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    String errorCode = "NOT_FOUND";

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        errorCode,
        ex.getMessage()
    );
    return ResponseEntity.status(status).body(errorResponse);
  }

  /**
   * 型変換エラー（{@code HttpMessageNotReadableException}）を処理する。
   *
   * @param ex 発生した {@code HttpMessageNotReadableException}
   * @return HTTP 400 BAD REQUEST と入力型エラーレスポンス
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatchException(
      HttpMessageNotReadableException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "TYPE_MISMATCH_ERROR",
        "入力のデータ型が不正です。"
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * バリデーションエラー（{@code MethodArgumentNotValidException}）を処理する。
   *
   * @param ex 発生した {@code MethodArgumentNotValidException}
   * @return HTTP 400 BAD REQUEST とバリデーションエラーレスポンス
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> message = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      message.put(error.getField(), error.getDefaultMessage());
    }

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "VALIDATION_ERROR",
        "入力のデータが不正です。",
        message
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * @PathVariableや@RequestParamのバリデーションエラー（{@code ConstraintViolationException}）を処理する。
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    Map<String, String> message = ex.getConstraintViolations().stream()
        .collect(Collectors.toMap(
            violation -> violation.getPropertyPath().toString(),
            ConstraintViolation::getMessage
        ));

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "VALIDATION_ERROR",
        "入力のデータが不正です。",
        message
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResponseStatusException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "NOT_FOUND",
        ex.getReason()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }
}