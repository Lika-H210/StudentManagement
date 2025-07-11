package raisetech.StudentManagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
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
    List<CourseStatus> courseStatusList = new ArrayList<>();
    List<CourseDetail> courseDetailList = new ArrayList<>();
    when(repository.searchStudentList()).thenReturn(studentList);
    when(repository.searchStudentCourseList()).thenReturn(studentsCourseList);
    when(repository.searchCourseStatusList()).thenReturn(courseStatusList);
    when(converter.convertToCourseDetail(studentsCourseList, courseStatusList))
        .thenReturn(courseDetailList);

    sut.searchStudentDetailList();

    verify(repository, times(1)).searchStudentList();
    verify(repository, times(1)).searchStudentCourseList();
    verify(repository, times(1)).searchCourseStatusList();
    verify(converter, times(1))
        .convertToCourseDetail(studentsCourseList, courseStatusList);
    verify(converter, times(1))
        .convertToStudentDetail(studentList, courseDetailList);
  }

  //受講生個人検索：正常系:studentに紐づくコース情報あり
  @Test
  void 受講生詳細個人取得処理でrepositoryおよびconverterが適切に呼び出されかつStudentDetailを返していること() {
    //事前準備
    Integer studentId = 999;
    String publicId = "00000000-0000-0000-0000-000000000000";
    student.setStudentId(studentId);
    Integer courseId = 888;
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(courseId);
    CourseStatus courseStatus = new CourseStatus();

    List<StudentCourse> studentCourseList = List.of(studentCourse);
    List<CourseStatus> courseStatusList = List.of(courseStatus);
    List<CourseDetail> courseDetailList = List.of(new CourseDetail(studentCourse, courseStatus));

    //期待値
    List<Integer> expectedCourseIdList = List.of(courseId);
    StudentDetail expected = new StudentDetail(student, courseDetailList);

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.searchStudentCourseListByStudentId(studentId)).thenReturn(studentCourseList);
    when(repository.searchCourseStatusListByCourseIdList(expectedCourseIdList)).thenReturn(
        courseStatusList);
    when(converter.convertToCourseDetail(studentCourseList, courseStatusList))
        .thenReturn(courseDetailList);

    //実行
    StudentDetail actual = sut.searchStudentDetailByPublicId(publicId);

    //検証
    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, times(1)).searchStudentCourseListByStudentId(studentId);
    //repositoryの実行確認と共に期待通りのcourseIdListで処理できているかも確認
    verify(repository, times(1)).searchCourseStatusListByCourseIdList(expectedCourseIdList);
    verify(converter, times(1)).convertToCourseDetail(studentCourseList, courseStatusList);
    assertThat(actual).isEqualTo(expected);
  }

  //受講生個人検索：正常系:studentに紐づくコース情報なしで早期リターン
  @Test
  void 受講生詳細個人取得処理で紐づくコース情報がない場合に早期リターンでCourseDetailが空リストのstudentDetailを返していること() {
    Integer studentId = 999;
    String publicId = "00000000-0000-0000-0000-000000000000";
    student.setStudentId(studentId);

    StudentDetail expected = new StudentDetail(student, List.of());

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.searchStudentCourseListByStudentId(studentId)).thenReturn(List.of());

    StudentDetail actual = sut.searchStudentDetailByPublicId(publicId);

    verify(repository, never()).searchCourseStatusListByCourseIdList(anyList());
    assertThat(actual).isEqualTo(expected);
  }

  //受講生登録処理：正常系
  @Test
  void 受講生詳細登録処理でrepositoryとクラス内メソッドの呼び出し及びpublicIdの付与が適切に行われていること()
      throws NotUniqueException {
    Integer studentId = 999;
    String email = "test@example.com";
    student.setEmail(email);
    StudentCourse studentCourse = new StudentCourse();
    CourseDetail courseDetail = new CourseDetail(studentCourse, null);
    StudentDetail studentDetail = new StudentDetail(student, List.of(courseDetail));

    when(repository.existsByEmail(email)).thenReturn(false);
    doAnswer(invocation -> {
      Student argStudent = invocation.getArgument(0);
      argStudent.setStudentId(studentId);
      return null;
    }).when(repository).registerStudent(studentDetail.getStudent());

    StudentDetail actual = sut.registerStudentDetail(studentDetail);

    verify(repository, times(1)).existsByEmail(email);
    verify(repository, times(1)).registerStudent(student);
    verify(repository, times(1)).registerStudentCourse(studentCourse);
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
    verify(repository, never()).registerStudent(any(Student.class));
  }

  //受講コース登録処理：正常系(複数コース登録）
  //Todo:メソッド名要変更
  @Test
  void 受講コースの登録で複数コース登録時にripositoryとコース情報初期設定メソッドが登録回数分呼び出されていること() {
    Integer studentId = 999;
    CourseDetail courseDetail1 = new CourseDetail(new StudentCourse(), null);
    CourseDetail courseDetail2 = new CourseDetail(new StudentCourse(), null);
    List<CourseDetail> courseDetailList = List.of(courseDetail1, courseDetail2);

    sut.registerStudentCourse(studentId, courseDetailList);

    verify(repository, times(courseDetailList.size())).registerStudentCourse(
        any(StudentCourse.class));
    //Todo:Status登録処理のrepository検証を追加
    assertThat(courseDetailList)
        .hasSize(courseDetailList.size())
        .allSatisfy(courseDetail -> {
          assertThat(courseDetail.getStudentCourse().getStudentId())
              .isEqualTo(studentId);
        });
    //Todo:Statusの初期値の設定メソッドの動作検証を追加
  }

  //受講コース登録処理：正常系(コース情報 空)
  @Test
  void 受講コースの登録でコースリストが空の時にripositoryは呼び出されないこと() {
    Integer studentId = 999;
    List<CourseDetail> courseDetailList = List.of();

    sut.registerStudentCourse(studentId, courseDetailList);

    verify(repository, never()).registerStudentCourse(any(StudentCourse.class));
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
  void 受講コース情報初期値としてstartDateがnullの場合はendDateもnullであること() {
    Integer studentId = 999;
    StudentCourse studentCourse = new StudentCourse();

    sut.initializeStudentCourse(studentId, studentCourse);

    assertNull(studentCourse.getEndDate());
  }

  //Todo:courseStatusの初期設定のテスト

  //受講生更新処理：正常系(複数コース詳細情報を更新）
  @Test
  void 受講生詳細情報の更新処理で必要なrepositoryが適切な回数だけ呼び出されていること()
      throws NotUniqueException {
    String publicId = "00000000-0000-0000-0000-000000000000";
    String email = "test@example.com";
    student.setEmail(email);
    student.setPublicId(publicId);
    CourseDetail courseDetail1 = new CourseDetail(new StudentCourse(), new CourseStatus());
    CourseDetail courseDetail2 = new CourseDetail(new StudentCourse(), new CourseStatus());
    List<CourseDetail> courseDetailList = List.of(courseDetail1, courseDetail2);
    StudentDetail studentDetail = new StudentDetail(student, courseDetailList);

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.existsByEmailExcludingPublicId(publicId, email)).thenReturn(false);

    sut.updateStudentDetail(studentDetail);

    verify(repository, times(1)).searchStudentByPublicId(publicId);
    verify(repository, times(1))
        .existsByEmailExcludingPublicId(publicId, email);
    verify(repository, times(1)).updateStudent(student);
    verify(repository, times(courseDetailList.size())).updateStudentCourse(
        any(StudentCourse.class));
    //Todo:ステータス更新処理のrepositoryの検証
  }

  //受講生更新処理：正常系(空コース詳細情報リストで更新）
  @Test
  void 受講生詳細情報の更新処理で受講コース情報のrepositoryが実行されないこと()
      throws NotUniqueException {
    String publicId = "00000000-0000-0000-0000-000000000000";
    String email = "test@example.com";
    student.setEmail(email);
    student.setPublicId(publicId);
    StudentDetail studentDetail = new StudentDetail(student, List.of());

    when(repository.searchStudentByPublicId(publicId)).thenReturn(student);
    when(repository.existsByEmailExcludingPublicId(publicId, email)).thenReturn(false);

    sut.updateStudentDetail(studentDetail);

    verify(repository, never()).updateStudentCourse(any(StudentCourse.class));
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
    verify(repository, never()).updateStudent(student);
  }

}