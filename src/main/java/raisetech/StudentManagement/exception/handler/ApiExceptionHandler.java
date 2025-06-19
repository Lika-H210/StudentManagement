package raisetech.StudentManagement.exception.handler;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raisetech.StudentManagement.exception.converter.ErrorResponseConverter;
import raisetech.StudentManagement.exception.custom.IllegalResourceAccessException;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.exception.custom.TestException;
import raisetech.StudentManagement.exception.response.ErrorResponse;

/**
 * REST APIに関連する例外を一元的に処理するハンドラークラスです。 各例外を適切な形式のErrorResponseに変換して返します。
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

  private ErrorResponseConverter converter;

  public ApiExceptionHandler(ErrorResponseConverter converter) {
    this.converter = converter;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleRequestBodyValidation(
      MethodArgumentNotValidException ex) {

    // 開発者向けログ出力
    log.warn("Validation error occurred", ex);

    // field毎エラーメッセージをレスポンス用に整理
    Map<String, List<String>> fieldErrors = converter.convertFieldErrorsFromBindingResult(
        ex.getBindingResult().getFieldErrors());

    //最終表示内容
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleRequestParamValidation(
      ConstraintViolationException ex) {

    // 開発者向けログ出力
    log.warn("Validation error occurred", ex);

    // fieldエラーメッセージをレスポンス用に整理
    Map<String, List<String>> fieldErrors = converter.convertFieldErrorsFromConstraintViolations(
        ex.getConstraintViolations());

    //最終表示内容
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  @ExceptionHandler(NotUniqueException.class)
  public ResponseEntity<ErrorResponse> handleNotUniqueException(NotUniqueException ex) {
    // 開発者向けログ出力
    log.warn("Duplicate value error: {}", ex.getMessage(), ex);

    //表示内容
    HttpStatus status = HttpStatus.CONFLICT;
    ErrorResponse errorResponse = new ErrorResponse(status, ex.getMessage());

    return ResponseEntity.status(status).body(errorResponse);
  }

  @ExceptionHandler(IllegalResourceAccessException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      IllegalResourceAccessException ex) {
    // 開発者向けログ出力
    log.error("Attempted to access a resource that should exist but was not found: {}",
        ex.getMessage(), ex);

    //表示内容
    HttpStatus status = HttpStatus.NOT_FOUND;
    ErrorResponse errorResponse = new ErrorResponse(status, ex.getMessage());

    return ResponseEntity.status(status).body(errorResponse);
  }

  //Todo:Exception.classの想定外に発生した例外を一括対応する@ExceptionHandlerの実装
  //Todo:RuntimeException.classの想定外に発生した例外を一括対応する@ExceptionHandlerの実装

  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> exceptionHandler(TestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

}
