package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.custom.IllegalResourceAccessException;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  private Student student;

  @BeforeEach
  void setUp() {
    sut = new StudentService(repository, converter);
    student = new Student();
  }

  //受講生全件検索：正常系
  @Test
  void 受講生詳細情報の全件取得処理でrepositoryとconverterを適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentsCourseList = new ArrayList<>();
    when(repository.searchStudentList()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentsCourseList);

    sut.searchStudentDetailList();

    verify(repository, times(1)).searchStudentList();
    verify(repository, times(1)).searchStudentCourseList();
    verify(converter, times(1))
        .convertToStudentDetail(studentList, studentsCourseList);
  }

  //受講生個人検索：正常系
  @Test
  void 受講生詳細個人取得処理でrepositoryが適切に呼び出されかつStudentDetailを返していること() {
    Integer studentId = 999;
    String publicId = "00000000-0000-0000-0000-000000000000";
    student.setStudentId(studentId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    StudentDetail expected = new StudentDetail(student, studentCourseList);

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.searchStudentCourseListByStudentId(studentId)).thenReturn(studentCourseList);

    StudentDetail actual = sut.searchStudentDetailByPublicId(publicId);

    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, times(1)).searchStudentCourseListByStudentId(studentId);
    assertEquals(expected, actual);
  }

  //受講生個人検索：例外系(404：IllegalResourceAccessException:未登録publicIDで受講生を検索した場合)
  @Test
  void 受講生詳細個人取得処理で未登録publicIdで検索した場合に例外が生じ処理が中断されること() {
    String publicId = "00000000-0000-0000-0000-000000000000";

    when(repository.searchStudentByPublicId(publicId)).thenReturn(null);

    IllegalResourceAccessException thrown = assertThrows(
        IllegalResourceAccessException.class,
        () -> sut.searchStudentDetailByPublicId(publicId)
    );
    assertEquals("受講生情報の取得中に問題が発生しました。システム管理者までご連絡ください。",
        thrown.getMessage());
    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, never()).searchStudentCourseListByStudentId(anyInt());
  }


  //受講生登録処理：正常系
  @Test
  void 受講生詳細登録処理でrepositoryとクラス内メソッドの呼び出し及びpublicIdの付与が適切に行われていること()
      throws NotUniqueException {
    Integer studentId = 999;
    String email = "test@example.com";
    student.setEmail(email);
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());

    when(repository.existsByEmail(email)).thenReturn(false);
    doAnswer(invocation -> {
      Student argStudent = invocation.getArgument(0);
      argStudent.setStudentId(studentId);
      return null;
    }).when(repository).registerStudent(studentDetail.getStudent());

    StudentService spyService = spy(sut);

    StudentDetail actual = spyService.registerStudentDetail(studentDetail);

    verify(repository, times(1)).existsByEmail(email);
    verify(repository, times(1)).registerStudent(student);
    verify(spyService, times(1)).registerStudentCourse(studentId,
        studentDetail.getStudentCourseList());
    assertNotNull(actual.getStudent().getPublicId());
    assertDoesNotThrow(() -> UUID.fromString(actual.getStudent().getPublicId()));
  }

  //受講生登録処理：例外系(400:NotUniqueException:登録済みemailの登録)
  @Test
  void 受講生詳細情報登録で登録済みemailで登録処理を実行した場合に例外が生じ処理が中断されること() {
    String email = "test@example.com";
    student.setEmail(email);
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());

    when(repository.existsByEmail(email)).thenReturn(true);

    NotUniqueException thrown = assertThrows(
        NotUniqueException.class,
        () -> sut.registerStudentDetail(studentDetail)
    );
    assertEquals("このメールアドレスは既に登録されています", thrown.getMessage());
    verify(repository, times(1)).existsByEmail(email);
    verify(repository, never()).registerStudent(any(Student.class));
  }

  //受講コース登録処理：正常系
  @ParameterizedTest
  @MethodSource("courseListTestCase")
  void 受講コースの登録でコース登録ripository及び初期値設定メソッドがコースリストサイズ分だけ呼び出されていること(
      List<StudentCourse> studentCourseList) {
    Integer studentId = 999;

    StudentService spyService = spy(sut);

    spyService.registerStudentCourse(studentId, studentCourseList);

    verify(repository, times(studentCourseList.size())).registerStudentCourse(
        any(StudentCourse.class));
    verify(spyService, times(studentCourseList.size())).initializeStudentCourse(eq(studentId),
        any(StudentCourse.class));
  }

  //受講コース登録処理：登録項目の初期値設定(StudentCourseがstartDateを含む場合)
  @Test
  void 受講コース情報初期値としてstudentIdとendDateがstudentCourseに反映されていること() {
    Integer studentId = 999;
    LocalDate startDate = LocalDate.of(2025, 7, 1);
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStartDate(startDate);

    sut.initializeStudentCourse(studentId, studentCourse);

    assertEquals(studentId, studentCourse.getStudentId());
    assertEquals(startDate.plusMonths(6), studentCourse.getEndDate());
  }

  //受講コース登録処理：登録項目の初期値設定(StudentCourseのstartDateがnullの場合)
  @Test
  void 受講コース情報初期値としてstudentIdがstudentCourseに反映されendDateはnullであること() {
    Integer studentId = 999;
    StudentCourse studentCourse = new StudentCourse();

    sut.initializeStudentCourse(studentId, studentCourse);

    assertEquals(studentId, studentCourse.getStudentId());
    assertNull(studentCourse.getEndDate());
  }

  //受講生更新処理：正常系
  @ParameterizedTest
  @MethodSource("courseListTestCase")
  void 受講生詳細情報の更新処理で必要なrepositoryが適切な回数だけ呼び出されていること(
      List<StudentCourse> studentCourseList) throws NotUniqueException {
    String publicId = "00000000-0000-0000-0000-000000000000";
    String email = "test@example.com";
    student.setEmail(email);
    student.setPublicId(publicId);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.existsByEmailExcludingPublicId(publicId, email)).thenReturn(false);

    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, times(1))
        .existsByEmailExcludingPublicId(publicId, email);
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(studentCourseList.size())).updateStudentCourse(
        any(StudentCourse.class));
  }

  //受講生更新処理：例外系(404:IllegalResourceAccessException:未登録のpublicIDで更新処理を実行した場合)
  @Test
  void 受講生更新処理で未登録publicIdを参照し更新した場合に例外が生じ処理が中断されること() {
    String publicId = "00000000-0000-0000-0000-000000000000";
    String email = "test@example.com";
    student.setEmail(email);
    student.setPublicId(publicId);
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());

    when(repository.searchStudentByPublicId(publicId)).thenReturn(null);

    IllegalResourceAccessException thrown = assertThrows(
        IllegalResourceAccessException.class,
        () -> sut.updateStudentDetail(studentDetail));

    assertEquals("受講生情報の取得中に問題が発生しました。システム管理者までご連絡ください。",
        thrown.getMessage());
    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, never()).existsByEmailExcludingPublicId(publicId, email);
    verify(repository, never()).updateStudent(student);
  }

  //受講生更新処理：例外系(400:NotUniqueException:登録済みemailで更新処理した場合)
  @Test
  void 受講生詳細情報の更新処理でDB登録済みのemailで更新実行した場合に例外が生じ処理が中断されること() {
    String publicId = "00000000-0000-0000-0000-000000000000";
    String email = "test@example.com";
    student.setEmail(email);
    student.setPublicId(publicId);
    StudentDetail studentDetail = new StudentDetail(student, new ArrayList<>());

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.existsByEmailExcludingPublicId(publicId, email)).thenReturn(true);

    NotUniqueException thrown = assertThrows(
        NotUniqueException.class,
        () -> sut.updateStudentDetail(studentDetail)
    );

    assertEquals("このメールアドレスは使用できません。", thrown.getMessage());
    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, times(1))
        .existsByEmailExcludingPublicId(publicId, email);
    verify(repository, never()).updateStudent(student);
  }

  //コース登録・更新処理でStudentCourseListの情報の有or空によらず正常に処理が行えるか確認するためのテストケース
  // (StudentDetailのstudentCourseList=nullはバリデーションで除外されるため検査対象外としました）
  private static Stream<List<StudentCourse>> courseListTestCase() {
    return Stream.of(
        List.of(new StudentCourse(), new StudentCourse()),
        List.of()
    );
  }
}