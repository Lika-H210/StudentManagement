package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.domain.condition.SearchCondition;

public class SearchConditionValidationTest {

  @Autowired
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  //検索条件：正常系
  @Test
  void 検索条件の入力チェックで異常が発生しないこと() {
    SearchCondition condition = SearchCondition.builder()
        .fullName("太郎")
        .kanaName("タロウ")
        .email("tanaka@example.com")
        .region("東京")
        .minAge(18)
        .maxAge(50)
        .sex("男性")
        .course("Javaコース")
        .status("受講中")
        .build();

    Set<ConstraintViolation<SearchCondition>> violations = validator.validate(condition);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("searchConditionValidPattern")
  void 検索条件のテストケースについて入力チェックで一つの異常が発生すること(
      SearchCondition condition,
      String message) {

    Set<ConstraintViolation<SearchCondition>> violations = validator.validate(condition);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains(message);
  }

  private static Stream<Arguments> searchConditionValidPattern() {
    return Stream.of(
        Arguments.of(SearchCondition.builder().fullName("a".repeat(51)).build(),
            "氏名の検索値は50文字以内で入力してください"),
        Arguments.of(SearchCondition.builder().kanaName("田中").build(),
            "カナ名の検索値は ひらがな・カタカナ、またはスペースのみを入力してください"),
        Arguments.of(SearchCondition.builder().email("tanaka").build(),
            "Emailは完全一致検索です。正しいメールアドレスの形式で入力してください"),
        Arguments.of(SearchCondition.builder().maxAge(1500).build(),
            "年齢の検索値は150以下で入力してください")
    );
  }

}
