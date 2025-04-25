package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static raisetech.StudentManagement.testutil.TestDataFactory.createInitCourseStatus;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormalSimple;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormalSimple;

import java.time.LocalDate;
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
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
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
  private Integer courseId1;
  private Integer courseId2;
  private List<Integer> courseIdList;
  private StudentCourse course1;
  private StudentCourse course2;
  private List<StudentCourse> studentCourseList;
  private CourseStatus initStatus1;
  private CourseStatus initStatus2;
  private List<CourseStatus> courseStatusList;
  private StudentDetail studentDetail;
  private CourseDetail courseDetail1;
  private CourseDetail courseDetail2;
  private List<CourseDetail> courseDetailList;

  @BeforeEach
  void setUp() {
    // テスト対象のクラス（Service）の初期化
    sut = new StudentService(repository, converter);

    //受講生情報の準備
    studentId = 999;
    student = createStudentNormalSimple(studentId);

    //受講コース情報(リスト)の準備
    courseId1 = 999;
    courseId2 = 9999;
    course1 = createStudentCourseNormalSimple(courseId1, studentId);
    course2 = createStudentCourseNormalSimple(courseId2, studentId);
    studentCourseList = List.of(course1, course2);
    courseIdList = List.of(courseId1, courseId2);

    //受講コース申込状況(リスト)の準備
    initStatus1 = createInitCourseStatus(999, courseId1);
    initStatus2 = createInitCourseStatus(9999, courseId2);
    courseStatusList = List.of(initStatus1, initStatus2);

    //受講コース詳細情報(リスト)の準備
    courseDetail1 = new CourseDetail(course1, initStatus1);
    courseDetail2 = new CourseDetail(course2, initStatus2);
    courseDetailList = List.of(courseDetail1, courseDetail2);

    //受講コース情報の準備
    studentDetail = new StudentDetail(student, courseDetailList);
  }

  // テストデータを提供するメソッド
  static Stream<List<StudentCourse>> provideStudentCourseLists() {
    return Stream.of(null, Collections.emptyList());
  }

  @Test
  void 受講生詳細情報の一覧検索_repositoryとconverterの処理が適切に呼び出せていること() {
    when(repository.searchStudents()).thenReturn(List.of(student));
    when(repository.searchStudentsCourses()).thenReturn(studentCourseList);
    when(repository.searchCoursesStatus()).thenReturn(courseStatusList);
    when(converter.convertCourseDetailList(studentCourseList, courseStatusList)).thenReturn(
        courseDetailList);
    when(converter.convertStudentDetailList(List.of(student), courseDetailList)).thenReturn(
        List.of(studentDetail));

    List<StudentDetail> actual = sut.getStudentDetailList();

    verify(repository, times(1)).searchStudents();
    verify(repository, times(1)).searchStudentsCourses();
    verify(repository, times(1)).searchCoursesStatus();
    verify(converter, times(1)).convertCourseDetailList(studentCourseList, courseStatusList);
    verify(converter, times(1)).convertStudentDetailList(List.of(student), courseDetailList);
  }

  @Test
  void 個人の受講生詳細情報の検索_StudentとCourseDetailがあるの場合_repositoryとconverterが適切に呼出され且つStudentDetailが適切に生成されていること() {
    when(repository.searchStudentByStudentId(studentId)).thenReturn(student);
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);
    when(repository.searchCourseStatusByCourseIdList(List.of(courseId1, courseId2))).thenReturn(
        courseStatusList);
    when(converter.convertCourseDetailList(studentCourseList, courseStatusList)).thenReturn(
        courseDetailList);

    StudentDetail actual = sut.getStudentDetail(studentId);

    assertNotNull(actual);
    assertEquals(studentDetail, actual);

    verify(repository, times(1)).searchStudentByStudentId(studentId);
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
    //getCourseDetailListの呼び出し確認としてconvertCourseDetailList呼び出しを代用
    verify(converter, times(1)).convertCourseDetailList(studentCourseList, courseStatusList);
  }

  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void 個人の受講生詳細情報の検索_StudentありでCourseDetailがnullまたはEmptyの場合_repositoryのみ適切に呼出され且つStudentDetailの空リストが生成されていること(
      List<StudentCourse> studentCourseList) {
    when(repository.searchStudentByStudentId(studentId)).thenReturn(student);
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);

    StudentDetail actual = sut.getStudentDetail(studentId);

    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertNotNull(actual.getCourseDetailList());
    assertTrue(actual.getCourseDetailList().isEmpty());

    verify(repository, times(1)).searchStudentByStudentId(studentId);
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
    verify(converter, never()).convertCourseDetailList(any(), any());
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
    CourseDetail testCourseDetail1 = new CourseDetail(course1, null);
    CourseDetail testCourseDetail2 = new CourseDetail(course2, null);
    StudentDetail testStudentDetail = new StudentDetail(student,
        List.of(testCourseDetail1, testCourseDetail2));
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);
    when(repository.searchCourseStatusByCourseIdList(List.of(courseId1, courseId2))).thenReturn(
        courseStatusList);
    when(converter.convertCourseDetailList(studentCourseList, courseStatusList)).thenReturn(
        courseDetailList);

    StudentDetail actual = sut.registerStudentDetail(testStudentDetail);

    assertNotNull(actual);
    assertEquals(studentDetail, actual);

    verify(repository, times(1)).registerStudent(student);
    //registerCourseDetailListが実行されたかを代替検証
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
  }

  //受講生登録処理2
  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void 受講生情報のみの登録処理_コース詳細情報がnullまたはEmpty_repositoryの処理が適切に呼び出され且つregisterStudentCoursesは呼び出されないこと(
      List<CourseDetail> courseDetailList) {
    StudentDetail studentDetail = new StudentDetail(student, courseDetailList);

    StudentDetail actual = sut.registerStudentDetail(studentDetail);

    assertNotNull(actual);
    assertEquals(student, actual.getStudent());
    assertTrue(actual.getCourseDetailList().isEmpty());

    verify(repository, times(1)).registerStudent(student);
    verify(repository, never()).searchStudentCoursesByStudentId(anyInt());
  }

  //受講コース情報登録処理
  @Test
  void 受講コース情報の登録処理_コース情報の作成を行いかつrepositoryとgetCourseDetailListの処理が適切に呼び出せていること() {
    when(repository.searchStudentCoursesByStudentId(studentId)).thenReturn(studentCourseList);
    when(repository.searchCourseStatusByCourseIdList(courseIdList)).thenReturn(
        courseStatusList);
    when(converter.convertCourseDetailList(studentCourseList, courseStatusList)).thenReturn(
        courseDetailList);

    List<CourseDetail> actual = sut.registerCourseDetailList(studentId, courseDetailList);

    assertNotNull(actual);
    assertEquals(courseDetailList, actual);

    verify(repository, times(studentCourseList.size())).registerStudentCourse(
        any(StudentCourse.class));
    verify(repository, times(courseStatusList.size())).registerCourseStatus(
        any(CourseStatus.class));
    verify(repository, times(1)).searchStudentCoursesByStudentId(studentId);
    //getCourseDetailListの呼出確認をconverterの呼出で代替
    verify(converter, times(1))
        .convertCourseDetailList(studentCourseList, courseStatusList);
  }

  //受講コースの新規作成処理
  @Test
  void 登録するコース情報の作成_正常に受講コース情報を作成できていること() {
    LocalDate today = LocalDate.now();
    CourseStatus courseStatus = StudentService.initCourseStatus(courseId1);

    assertEquals(courseId1, courseStatus.getCourseId());
    assertEquals(courseStatus.getStatus(), "仮申込");
    assertEquals(today, courseStatus.getProvisionalApplicationDate());
  }

  //受講生詳細情報更新処理1
  @Test
  void 受講生詳細情報の更新_受講生情報ありコース詳細情報ありの場合_repositoryの処理が適切に呼び出せていること() {
    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(studentDetail.getCourseDetailList().size())).updateStudentCourse(
        any(StudentCourse.class));
    verify(repository, times(studentDetail.getCourseDetailList().size())).updateCourseStatus(
        any(CourseStatus.class));
  }

  //受講生詳細情報更新処理2
  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void 受講生詳細情報の更新_受講生情報ありコース詳細情報がnullまたはEmpty_repositoryの処理が適切に呼び出せていること(
      List<CourseDetail> courseDetailList) {
    StudentDetail studentDetail = new StudentDetail(student, courseDetailList);

    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).updateStudent(student);
    verify(repository, never()).updateStudentCourse(any(StudentCourse.class));
    verify(repository, never()).updateCourseStatus(any(CourseStatus.class));
  }

  @Test
  void コース詳細情報作成_受講コース情報が存在する場合_repositoryとconverterが適切に呼出されていること() {
    when(repository.searchCourseStatusByCourseIdList(courseIdList)).thenReturn(courseStatusList);
    when(converter.convertCourseDetailList(studentCourseList, courseStatusList)).thenReturn(
        courseDetailList);

    List<CourseDetail> actual = sut.getCourseDetailList(studentCourseList);

    assertNotNull(actual);
    assertEquals(courseDetailList, actual);

    verify(repository, times(1)).searchCourseStatusByCourseIdList(
        courseIdList);
    verify(converter, times(1)).convertCourseDetailList(studentCourseList, courseStatusList);
  }

  @ParameterizedTest
  @MethodSource("provideStudentCourseLists")
  void コース詳細情報作成_受講コース情報リストにnullまたは空リストが渡された場合_空リストが返る(
      List<StudentCourse> inputStudentCourseList) {
    List<CourseDetail> actual = sut.getCourseDetailList(inputStudentCourseList);

    assertNotNull(actual);
    assertTrue(actual.isEmpty());
  }
}