package raisetech.StudentManagement.controller;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.testdata.TestDataFactory;
import raisetech.StudentManagement.testdata.ValidationTestDataFactory;
import raisetech.StudentManagement.validation.UpdateGroup;

/**
 * バリデーションの制約が正しく機能することを確認するためのテストクラスです。
 */
public class StudentControllerValidationTest {

  @Autowired
  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  private TestDataFactory factory;

  @BeforeEach
  void setUp() {
    factory = new TestDataFactory();
  }

  @Test
  void 更新処理に対応するStudentの入力チェックで異常が発生しないこと() {
    Student student = factory.createBaseStudent();
    student.setNickname("たろ");
    student.setRegion("東京都 千代田区");
    student.setAge(35);
    student.setSex("男性");
    student.setRemark("特になし");

    Set<ConstraintViolation<Student>> violations = validator.validate(student, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  @ParameterizedTest
  @MethodSource("studentValidPattern")
  void 更新処理においてStudentの各テストケースについて入力チェックで一つの異常が発生すること(
      Student student, String message) {
    Set<ConstraintViolation<Student>> violations = validator.validate(student, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains(message);
  }

  private static Stream<Arguments> studentValidPattern() {
    ValidationTestDataFactory dataFactory = new ValidationTestDataFactory();
    return Stream.of(
        Arguments.of(dataFactory.withInvalidFullName(" "),
            "名前の入力は必須です"),
        Arguments.of(dataFactory.withInvalidFullName("あ".repeat(51)),
            "名前は50文字以内で入力してください"),
        Arguments.of(dataFactory.withInvalidEmail("test.example.com"),
            "正しいメールアドレスの形式で入力してください"),
        Arguments.of(dataFactory.withInvalidAge(-1),
            "年齢は0以上の数値を入力してください"),
        Arguments.of(dataFactory.withInvalidSex("男"),
            "男性・女性・その他 のいずれかを入力してください"));
  }

  @Test
  void 更新処理に対応するStudentCourseの入力チェックで異常が発生しないこと() {
    StudentCourse studentCourse = factory.createBaseCourse();
    LocalDate now = LocalDate.now();
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusMonths(6));

    Set<ConstraintViolation<StudentCourse>> violations = validator
        .validate(studentCourse, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  @ParameterizedTest
  @MethodSource("studentCourseValidPattern")
  void 更新処理においてStudentCourseの入力チェックで一つの異常が発生すること(
      StudentCourse studentCourse, String message) {
    Set<ConstraintViolation<StudentCourse>> violations = validator
        .validate(studentCourse, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains(message);
  }

  private static Stream<Arguments> studentCourseValidPattern() {
    ValidationTestDataFactory dataFactory = new ValidationTestDataFactory();
    return Stream.of(
        Arguments.of(dataFactory.withInvalidCourse(" "),
            "コース名の入力は必須です"),
        Arguments.of(dataFactory.withInvalidCourse("a".repeat(31)),
            "コース名は30文字以内で入力してください")
    );
  }

  @Test
  void 更新処理に対応するStudentDetailの入力チェックで異常が発生しないこと() {
    Student student = factory.createBaseStudent();
    StudentCourse studentCourse = factory.createBaseCourse();
    StudentDetail studentDetail = new StudentDetail(student, List.of(studentCourse));

    Set<ConstraintViolation<StudentDetail>> violations = validator
        .validate(studentDetail, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  @ParameterizedTest
  @MethodSource("studentDetailValidPattern")
  void 更新処理に対応するStudentDetailの入力チェックで一つの異常が発生すること(
      StudentDetail studentDetail, String message) {
    Set<ConstraintViolation<StudentDetail>> violations = validator
        .validate(studentDetail, UpdateGroup.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations).extracting("message").contains(message);
  }

  private static Stream<Arguments> studentDetailValidPattern() {
    TestDataFactory factory = new TestDataFactory();
    return Stream.of(
        Arguments.of(new StudentDetail(null, List.of(factory.createBaseCourse())),
            "受講生情報の入力は必須です。"),
        Arguments.of(new StudentDetail(factory.createBaseStudent(), null),
            "登録・更新処理に必要な情報が不足しています。システム管理者にご連絡ください。")
    );
  }

}
