package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormalSimple;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormalSimple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @InjectMocks
  private StudentService sut;

  // テスト用の共通オブジェクト
  private Integer studentId;
  private Student student;
  private StudentCourse course1;
  private StudentCourse course2;
  private List<StudentCourse> studentCourseList;
  private StudentDetail studentDetail;

  @BeforeEach
  void setUp() {
    // テスト対象のクラス（Service）の初期化
    sut = new StudentService(repository, converter);

    //受講生情報の準備
    studentId = 999;
    student = createStudentNormalSimple(studentId);

    //受講コース情報(リスト)の準備
    course1 = createStudentCourseNormalSimple(999, studentId);
    course2 = createStudentCourseNormalSimple(9999, studentId);
    studentCourseList = List.of(course1, course2);

    //受講コース情報の準備
    studentDetail = new StudentDetail(student, studentCourseList);
  }

  // テストデータを提供するメソッド
  static Stream<List<StudentCourse>> provideStudentCourseLists() {
    return Stream.of(null, Collections.emptyList());
  }

  @Test
  void 受講生詳細情報の一覧検索_repositoryとconverterの処理が適切に呼び出せていること() {
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    when(repository.searchStudents()).thenReturn(studentList);
    when(repository.searchStudentsCourses()).thenReturn(studentCourseList);

    List<StudentDetail> actual = sut.getStudentDetailList();

    verify(repository, times(1)).searchStudents();
    verify(repository, times(1)).searchStudentsCourses();
    verify(converter, times(1)).convertStudentDetailList(studentList, studentCourseList);
  }

  @Test
  void 個人の受講生詳細情報の検索_repositoryの処理の呼び出しとStudentDetailの生成が適切に行われていること() {
    when(repository.searchStudentByStudentId(studentId)).thenReturn(student);
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);

    StudentDetail actual = sut.getStudentDetail(studentId);

    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());

    verify(repository, times(1)).searchStudentByStudentId(studentId);
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
  }

  @Test
  void 個人の受講生詳細情報の検索_対象studentIdの受講生がいない場合ResponseStatusExceptionの例外処理が行われる() {
    when(repository.searchStudentByStudentId(studentId)).thenReturn(null);
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(Collections.emptyList());

    assertThrows(ResponseStatusException.class, () -> sut.getStudentDetail(studentId));

  }

  //受講生登録処理における受講コース情報の同時登録については受講コース情報登録処理(registerStudentCourses)の動作有無のみテスト
  //受講生登録処理におけるregisterStudentCoursesの動作有無の確認はsearchStudentCoursesByStudentIdで代用
  //受講生登録処理1
  @Test
  void 受講生詳細情報の登録処理_受講生情報と受講コース情報の同時登録時にrepository及びregisterStudentCoursesの処理が適切に呼び出せていること() {
    doReturn(studentCourseList).when(repository).searchStudentCoursesByStudentId(studentId);

    StudentDetail actual = sut.registerStudentDetail(studentDetail);

    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertEquals(studentCourseList, actual.getStudentCourseList());

    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
  }

  //受講生登録処理2
  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void 受講生情報のみの登録処理_コース情報がnullまたはempty_repositoryの処理が適切に呼び出され且つregisterStudentCoursesは呼び出されないこと(
      List<StudentCourse> studentCourseList) {
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    StudentDetail actual = sut.registerStudentDetail(studentDetail);

    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertTrue(actual.getStudentCourseList().isEmpty());

    verify(repository, times(1)).registerStudent(student);
    verify(repository, never()).searchStudentCoursesByStudentId(anyInt());
  }

  //受講コース情報登録処理
  @Test
  void 受講コース情報の登録処理_コース情報の作成を行いかつrepositoryの処理が適切に呼び出せていること() {
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);

    List<StudentCourse> actual = sut.registerStudentCourses(studentId, studentCourseList);

    assertNotNull(actual);
    assertEquals(studentCourseList, actual);

    verify(repository, times(studentCourseList.size())).registerStudentCourse(
        any(StudentCourse.class));

    for (StudentCourse course : studentCourseList) {
      assertEquals(studentId, course.getStudentId());
      assertNotNull(course.getStartDate());
      assertNotNull(course.getEndDate());
      assertEquals(course.getStartDate().plusYears(1), course.getEndDate()); // 1年後になっているか
    }

    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
  }

  //受講コースの新規作成処理
  @Test
  void 登録するコース情報の作成_正常に受講コース情報を作成できていること() {
    StudentService.initStudentCourse(studentId, course1);

    assertEquals(studentId, course1.getStudentId());
    assertNotNull(course1.getStartDate());
    assertNotNull(course1.getEndDate());
    assertEquals(course1.getStartDate().plusYears(1), course1.getEndDate());
  }

  //受講生詳細情報更新処理1
  @Test
  void 受講生詳細情報の更新_受講生情報あり受講コースありの場合_repositoryの処理が適切に呼び出せていること() {
    doNothing().when(repository).updateStudent(student);
    doNothing().when(repository).updateStudentCourse(any(StudentCourse.class));

    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(studentCourseList.size())).updateStudentCourse(
        any(StudentCourse.class));
  }

  //受講生詳細情報更新処理2
  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void 受講生詳細情報の更新_受講生情報あり受講コースがnullまたはempty_repositoryの処理が適切に呼び出せていること(
      List<StudentCourse> studentCourseList) {
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, never()).updateStudentCourse(any(StudentCourse.class));
  }

}