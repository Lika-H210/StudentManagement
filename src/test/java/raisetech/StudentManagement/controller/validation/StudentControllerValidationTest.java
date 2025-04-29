package raisetech.StudentManagement.controller.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raisetech.StudentManagement.testutil.TestDataFactory.createCourseStatusNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createInitCourseStatus;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormal;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.StudentManagement.controller.StudentController;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.criteria.StudentDetailSearchCriteria;
import raisetech.StudentManagement.service.StudentService;
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

  //Student1
  @Test
  void Studentクラスのバリデーション正常動作テスト() {
    Student registerNormalstudent = createStudentNormal(null);

    Set<ConstraintViolation<Student>> violations = validator.validate(registerNormalstudent,
        OnRegisterStudent.class);

    assertThat(violations).isEmpty();
  }

  //Student2
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

  //Student2のバリデーションエラーテストケース
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

  //StudentCourse1
  @Test
  void StudentCourseクラスのバリデーション正常動作テスト() {
    StudentCourse updateNormalCourse = createStudentCourseNormal(999, 999);
    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(updateNormalCourse,
        OnUpdate.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  //StudentCourse2
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

  //StudentCourse2のバリデーションエラーテストケース
  static Stream<Arguments> studentCourseValidationErrorCases() {
    //course,EndDate,エラーメッセージ
    return Stream.of(
        // @FutureOrPresent: 終了日が過去の日付
        Arguments.of(ValidationTestDataFactory.createStudentCourseWithPastEndDate(999, 999),
            "受講終了日は未来または現在の日付を入力してください。")
    );
  }

  //CourseStatus1
  @Test
  void CourseStatus_バリデーション正常動作テスト() {
    CourseStatus updateNormalStatus = createCourseStatusNormal(999, 999);
    Set<ConstraintViolation<CourseStatus>> violations = validator.validate(updateNormalStatus,
        OnUpdate.class);

    assertThat(violations).isEmpty();
  }

  //CourseStatus2
  @ParameterizedTest
  @MethodSource("courseStatusValidationErrorCases")
  void CourseStatus_バリデーションエラー動作テスト(CourseStatus courseStatus, String errorMessage) {
    Set<ConstraintViolation<CourseStatus>> violations = validator.validate(courseStatus,
        OnUpdate.class);

    assertThat(violations.size()).isEqualTo(1);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorMessage);
  }

  //CourseStatus2のバリデーションエラーテストケース
  static Stream<Arguments> courseStatusValidationErrorCases() {
    return Stream.of(
        Arguments.of(
            ValidationTestDataFactory.createCourseStatusWithIncorrectStatus(999, 999),
            "仮申込,本申込,受講中,受講完了,キャンセル のいずれかを入力してください"
        ),
        Arguments.of(
            ValidationTestDataFactory.createCourseStatusWithFutureProvisionalDate(999, 999),
            "過去または現在の日付を入力してください。"
        )
    );
  }

  //StudentDetail1
  @ParameterizedTest
  @MethodSource("studentCourseValidationNormalCases")
  void StudentDetail_バリデーション正常動作テスト(Student student,
      List<CourseDetail> courseDetailList) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseDetailList(courseDetailList);

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(studentDetail, OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(0);
  }

  //StudentDetail1のバリデーション正常テストケース
  static Stream<Arguments> studentCourseValidationNormalCases() {
    Student normalStudent = createStudentNormal(null);
    StudentCourse normalStudentCourse = createStudentCourseNormal(null, null);
    CourseDetail normalCourseDetail = new CourseDetail(normalStudentCourse, null);

    return Stream.of(
        Arguments.of(normalStudent, null),
        Arguments.of(normalStudent, List.of(normalCourseDetail))
    );
  }

  //StudentDetail2
  @ParameterizedTest
  @MethodSource("studentDetailValidationErrorCases")
  void StudentDetail_バリデーションエラー動作テスト_(Student student,
      List<CourseDetail> courseDetailList,
      String errorMessage) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setCourseDetailList(courseDetailList);

    Set<ConstraintViolation<StudentDetail>> violations =
        validator.validate(studentDetail, OnRegisterStudent.class);

    assertThat(violations.size()).isEqualTo(1);

    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorMessage);
  }

  //StudentDetail2のバリデーションエラーテストケース(個別validationの確認と@Valid連携確認)
  private static Stream<Arguments> studentDetailValidationErrorCases() {
    Student normalStudent = createStudentNormal(null);
    StudentCourse normalStudentCourse = createStudentCourseNormal(null, null);
    CourseDetail normalCourseDetail = new CourseDetail(normalStudentCourse, null);

    return Stream.of(
        // studentがnull
        Arguments.of(null, null, "受講生情報は必須です。"),
        // studentのfullNameがnull
        Arguments.of(ValidationTestDataFactory.createStudentForFullNameValidTest(null, null),
            List.of(normalCourseDetail),
            "氏名は必須です。"),
        // CourseDetailのstudentCourseが空
        Arguments.of(normalStudent, List.of(new CourseDetail()), "受講コース情報は必須です。")
    );
  }

  //CourseDetail1
  @Test
  void CourseDetail_バリデーション正常動作テスト() {
    StudentCourse registerNormalCourse = createStudentCourseNormal(null, null);
    CourseDetail courseDetail = new CourseDetail(registerNormalCourse, null);
    Set<ConstraintViolation<CourseDetail>> violations =
        validator.validate(courseDetail, OnRegisterStudent.class);

    assertThat(violations).isEmpty();
  }

  //CourseDetail2
  @ParameterizedTest
  @MethodSource("courseDetailValidationErrorCases")
  void CourseDetail_バリデーションエラー動作テスト(StudentCourse studentCourse,
      CourseStatus courseStatus,
      String errorMessage) {
    CourseDetail courseDetail = new CourseDetail(studentCourse, courseStatus);

    Set<ConstraintViolation<CourseDetail>> violations =
        validator.validate(courseDetail, OnUpdate.class);

    assertThat(violations.size()).isEqualTo(1);

    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly(errorMessage);
  }

  //CourseDetail2のバリデーションエラーテストケース(個別validationの確認と@Valid連携確認)
  private static Stream<Arguments> courseDetailValidationErrorCases() {
    StudentCourse updateNormalCourse = createStudentCourseNormal(999, 999);
    CourseStatus updateCourseStatus = createInitCourseStatus(999, 999);
    return Stream.of(
        // studentがnull
        Arguments.of(null, updateCourseStatus, "受講コース情報は必須です。"),
        Arguments.of(updateNormalCourse, null, "コース申込み状況は必須です。"),
        // studentのfullNameがnull
        Arguments.of(ValidationTestDataFactory.createStudentCourseWithNoCourse(999, 999),
            updateCourseStatus,
            "コース名は必須です。"),
        // CourseDetailのstudentCourseが空
        Arguments.of(updateNormalCourse,
            ValidationTestDataFactory.createCourseStatusWithFutureProvisionalDate(999, 999),
            "過去または現在の日付を入力してください。")
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"仮申込", "本申込", "受講中", "受講完了", "キャンセル"})
  void 検索条件_バリデーション正常動作テスト(String status) {
    StudentDetailSearchCriteria criteria = new StudentDetailSearchCriteria();
    criteria.setStatus(status);

    Set<ConstraintViolation<StudentDetailSearchCriteria>> violations = validator.validate(criteria);

    assertThat(violations).isEmpty();
  }

  @Test
  void 検索条件_バリデーション異常動作テスト() {
    StudentDetailSearchCriteria criteria = new StudentDetailSearchCriteria();
    criteria.setStatus("仮申込み");

    Set<ConstraintViolation<StudentDetailSearchCriteria>> violations = validator.validate(criteria);

    assertThat(violations.size()).isEqualTo(1);

    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsOnly("仮申込,本申込,受講中,受講完了,キャンセル のいずれかを入力してください");
  }


}
