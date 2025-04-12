package raisetech.StudentManagement.controller.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.controller.StudentController;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.testutil.TestDataFactory;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;

@WebMvcTest(StudentController.class)
class StudentControllerValidationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StudentService service;

  private Validator validator;

  @BeforeEach
  void setUp() {
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void 個人受講生詳細情報取得_studentIdが0の場合は400エラーを返す() throws Exception {
    mockMvc.perform(get("/student/0")
            .header("Accept-Language", "ja"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
        .andExpect(jsonPath("$.error").value("入力のデータが不正です。"))
        .andExpect(jsonPath("$.message['getStudentDetail.studentId']")
            .value("1 以上の値にしてください"));
  }

  @Test
  void Studentクラスのバリデーション正常動作テスト() {
    Student student = TestDataFactory.createStudentNormal(null);

    Set<ConstraintViolation<Student>> violations = validator.validate(student,
        OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  @ParameterizedTest
  @MethodSource("studentValidationErrorCases")
  void Studentクラスのバリデーションエラー動作テスト(Student student, String errorResponse) {

    Set<ConstraintViolation<Student>> violations = validator.validate(student,
        OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorResponse);
  }

  //Studentのバリデーションエラーテストケース
  static Stream<Arguments> studentValidationErrorCases() {
    //studentId,fullName,mailAddress,sex,エラーmessage
    return Stream.of(
        //@Null
        Arguments.of(ValidationTestDataFactory.createStudentForStudentIdValidTest(999),
            "新規登録時は受講生IDは不要です。"),
        //@Empty
        Arguments.of(ValidationTestDataFactory.createStudentForFullNameValidTest(null, null),
            "氏名は必須です。"),
        //@Size
        Arguments.of(
            ValidationTestDataFactory.createStudentForFullNameValidTest(null, "あ".repeat(101)),
            "氏名は100文字以内である必要があります。"),
        //@Email
        Arguments.of(
            ValidationTestDataFactory.createStudentForMailValidTest(null, "mori.hajime-email"),
            "正しいメールアドレスを入力してください。"),
        //@Pattern
        Arguments.of(ValidationTestDataFactory.createStudentForSexValidTest(null, "男"),
            "性別は 'Male', 'Female', 'Other' のいずれかを入力してください。")
    );
  }

  @Test
  void StudentCourseクラスのバリデーション正常動作テスト() {
    StudentCourse studentCourse = TestDataFactory.createStudentCourseNormal(999, 999);

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        OnUpdate.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  @ParameterizedTest
  @MethodSource("studentCourseValidationErrorCases")
  void StudentCourse_バリデーションエラー動作テスト(StudentCourse studentCourse,
      String errorMessage) {
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse,
        OnUpdate.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorMessage);
  }

  //StudentCourseのバリデーションエラーテストケース
  static Stream<Arguments> studentCourseValidationErrorCases() {
    //course,EndDate,エラーメッセージ
    return Stream.of(
        // @FutureOrPresent: 終了日が過去の日付
        Arguments.of(ValidationTestDataFactory.createStudentCourseWithPastEndDate(999, 999),
            "受講終了日は未来または現在の日付を入力してください。")
    );
  }

  @ParameterizedTest
  @MethodSource("studentCourseValidationNormalCases")
  void StudentDetail_バリデーション正常動作テスト(Student student, List<StudentCourse> courseList) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(courseList);

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(studentDetail, OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  //StudentDetailのバリデーション正常テストケース
  static Stream<Arguments> studentCourseValidationNormalCases() {
    Student validStudent = TestDataFactory.createStudentNormal(null);
    StudentCourse studentCourse = TestDataFactory.createStudentCourseNormal(null, null);

    return Stream.of(
        Arguments.of(validStudent, null),
        Arguments.of(validStudent, Collections.emptyList()),
        Arguments.of(validStudent, List.of(studentCourse))
    );
  }

  @ParameterizedTest
  @MethodSource("studentDetailValidationErrorCases")
  void StudentDetail_バリデーションエラー動作テスト(Student student, List<StudentCourse> courseList,
      String errorMessage) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setStudentCourseList(courseList);

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(studentDetail, OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(1);

    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorMessage);
  }

  //StudentDetailのバリデーションエラーテストケース
  private static Stream<Arguments> studentDetailValidationErrorCases() {
    return Stream.of(
        // studentがnull
        Arguments.of(null, List.of(), "受講生情報は必須です。"),
        // studentのfullNameがnull
        Arguments.of(ValidationTestDataFactory.createStudentForFullNameValidTest(null, null),
            List.of(),
            "氏名は必須です。"),
        // studentCourseのコース名がnull
        Arguments.of(TestDataFactory.createStudentNormal(null),
            List.of(ValidationTestDataFactory.createStudentCourseWithNoCourse(null, null)),
            "コース名は必須です。")
    );
  }
}
