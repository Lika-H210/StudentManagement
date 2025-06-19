package raisetech.StudentManagement.exception.converter;

import jakarta.validation.ConstraintViolation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

/**
 * 各種例外から取得したエラー情報を、クライアント向けのレスポンス形式に変換するコンバータークラスです。
 */
@Component
public class ErrorResponseConverter {

  public Map<String, List<String>> convertFieldErrorsFromBindingResult(
      List<FieldError> fieldErrorList) {
    return fieldErrorList.stream()
        .collect(Collectors.groupingBy(
            FieldError::getField,
            Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
        ));
  }

  public Map<String, List<String>> convertFieldErrorsFromConstraintViolations(
      Set<ConstraintViolation<?>> constraintViolations) {
    return constraintViolations.stream()
        .collect(Collectors.groupingBy(
            cv -> extractFieldNameFromPropertyPath(cv.getPropertyPath().toString()),
            Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
        ));
  }

  private String extractFieldNameFromPropertyPath(String path) {
    return Arrays.asList(path.split("\\.")).getLast();
  }

}
