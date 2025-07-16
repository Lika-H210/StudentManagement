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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.criteria.SearchCriteria;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;
import raisetech.StudentManagement.service.normalizer.SearchCriteriaNormalizer;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  @Mock
  private SearchCriteriaNormalizer normalizer;

  private StudentService sut;

  private Student student;

  @BeforeEach
  void setUp() {
    sut = new StudentService(repository, converter, normalizer);
    student = new Student();
  }

  //受講生詳細条件検索：正常系:検索条件カナ名のみ
  @Test
  void 受講生詳細条件検索で条件なしで検索した場合にrepositoryが正規化後の引数で適切に実行されかつ必要なconverterが呼び出されていること() {
    //前準備
    SearchCriteria criteria = SearchCriteria.builder().kanaName("やまだ").build();
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentsCourseList = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();
    List<CourseDetail> courseDetailList = new ArrayList<>();

    String normalizedKanaName = "ヤマダ";

    when(repository.searchStudentList(any(SearchCriteria.class))).thenReturn(studentList);
    when(repository.searchStudentCourseList(any(SearchCriteria.class)))
        .thenReturn(studentsCourseList);
    when(repository.searchCourseStatusList(any(SearchCriteria.class))).thenReturn(courseStatusList);
    when(converter.convertToCourseDetail(studentsCourseList, courseStatusList))
        .thenReturn(courseDetailList);
    when(normalizer.kanaNameNormalize(criteria.getKanaName())).thenReturn(normalizedKanaName);

    //期待値
    SearchCriteria expectCriteria = SearchCriteria.builder().kanaName(normalizedKanaName).build();

    //実行
    sut.searchStudentDetailList(criteria);

    //メソッド呼び出しの検証
    ArgumentCaptor<SearchCriteria> captor = ArgumentCaptor.forClass(SearchCriteria.class);
    verify(repository, times(1)).searchStudentList(captor.capture());
    verify(repository, times(1)).searchStudentCourseList(captor.capture());
    verify(repository, times(1)).searchCourseStatusList(captor.capture());
    verify(converter, times(1))
        .convertToCourseDetail(studentsCourseList, courseStatusList);
    verify(converter, times(1))
        .toStudentDetailFromAllStudents(studentList, courseDetailList);
    //repositoryの引数が正規化後のcriteriaであることの検証
    List<SearchCriteria> criteriaList = captor.getAllValues();
    assertThat(criteriaList)
        .hasSize(3)
        .allSatisfy(cr -> assertThat(cr.getKanaName()).isEqualTo(expectCriteria.getKanaName()));
  }

  //受講生詳細条件検索：正常系:コース検索条件あり
  @Test
  void 受講生詳細条件検索でコース条件ありで検索した場合に早期リターンで適切なconverterを呼び出せていること() {
    SearchCriteria criteria = SearchCriteria.builder().course("Java").build();
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentsCourseList = new ArrayList<>();
    List<CourseStatus> courseStatusList = new ArrayList<>();
    List<CourseDetail> courseDetailList = new ArrayList<>();

    when(repository.searchStudentList(any(SearchCriteria.class))).thenReturn(studentList);
    when(repository.searchStudentCourseList(any(SearchCriteria.class))).thenReturn(
        studentsCourseList);
    when(repository.searchCourseStatusList(any(SearchCriteria.class))).thenReturn(courseStatusList);
    when(converter.convertToCourseDetail(studentsCourseList, courseStatusList))
        .thenReturn(courseDetailList);

    sut.searchStudentDetailList(criteria);

    verify(converter, times(1))
        .toStudentDetailFromStudentsWithCourse(studentList, courseDetailList);
    verify(converter, never())
        .toStudentDetailFromAllStudents(anyList(), anyList());
  }

  //受講生詳細条件検索：正常系:検索条件と合致する受講生なし
  @Test
  void 受講生詳細条件検索で条件に合致する受講生がいない場合に早期リターンで空リストが返されること() {
    SearchCriteria criteria = SearchCriteria.builder().build();
    List<Student> studentList = List.of();

    when(repository.searchStudentList(any(SearchCriteria.class))).thenReturn(studentList);

    List<StudentDetail> actual = sut.searchStudentDetailList(criteria);

    verify(repository, never()).searchStudentCourseList(any(SearchCriteria.class));
    assertThat(actual).isEmpty();
  }

  //検索条件正規化処理
  @Test
  void kanaNameのみが正規化され他の値は保持されたcriteriaが返されること() {
    //非変換項目
    String fullName = "山田";
    String email = "yamada@example.com";
    String region = "東京都";
    Integer minAge = 20;
    Integer maxAge = 30;
    String sex = "男性";
    String course = "Java";
    String status = "受講中";

    //変換対象項目（変換前と変換後）
    String beforeKanaName = "やまだ";
    String afterKanaName = "ヤマダ";
    
    SearchCriteria criteria = SearchCriteria.builder()
        .fullName(fullName)
        .kanaName(beforeKanaName)  // 正規化対象
        .email(email)
        .region(region)
        .minAge(minAge)
        .maxAge(maxAge)
        .sex(sex)
        .course(course)
        .status(status)
        .build();

    SearchCriteria expected = SearchCriteria.builder()
        .fullName(fullName)
        .kanaName(afterKanaName) // 正規化済み
        .email(email)
        .region(region)
        .minAge(minAge)
        .maxAge(maxAge)
        .sex(sex)
        .course(course)
        .status(status)
        .build();

    when(normalizer.kanaNameNormalize(criteria.getKanaName())).thenReturn(afterKanaName);

    SearchCriteria actual = sut.normalizeCriteria(criteria);

    verify(normalizer, times(1)).kanaNameNormalize(beforeKanaName);
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
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

  //コース詳細情報の登録処理：正常系(複数コース登録時）
  @Test
  void コース詳細情報登録で複数コース登録時にリストサイズ分の各repositoryと初期設定メソッドが呼び出されていること() {
    Integer studentId = 999;
    CourseDetail courseDetail1 = new CourseDetail(new StudentCourse(), null);
    CourseDetail courseDetail2 = new CourseDetail(new StudentCourse(), null);
    List<CourseDetail> courseDetailList = List.of(courseDetail1, courseDetail2);

    sut.registerStudentCourse(studentId, courseDetailList);

    verify(repository, times(courseDetailList.size())).registerStudentCourse(
        any(StudentCourse.class));
    verify(repository, times(courseDetailList.size())).registerCourseStatus(
        any(CourseStatus.class));
    assertThat(courseDetailList)
        .hasSize(courseDetailList.size())
        .allSatisfy(courseDetail -> {
          assertThat(courseDetail.getStudentCourse().getStudentId())
              .isEqualTo(studentId);
          assertThat(courseDetail.getCourseStatus())
              .isNotNull();
        });
  }

  //受講コース登録処理：正常系(コース情報 空)
  @Test
  void 受講コースの登録でコースリストが空の時にripositoryは呼び出されないこと() {
    Integer studentId = 999;
    List<CourseDetail> courseDetailList = List.of();

    sut.registerStudentCourse(studentId, courseDetailList);

    verify(repository, never()).registerStudentCourse(any(StudentCourse.class));
  }

  //コース登録項目の初期値設定(StudentCourseがstartDateを含む場合)
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

  //コース登録項目の初期値設定(StudentCourseのstartDateがnullの場合)
  @Test
  void 受講コース情報初期値としてstartDateがnullの場合はendDateもnullであること() {
    Integer studentId = 999;
    StudentCourse studentCourse = new StudentCourse();

    sut.initializeStudentCourse(studentId, studentCourse);

    assertNull(studentCourse.getEndDate());
  }

  //コース申込ステータスの初期オブジェクト生成
  @Test
  void コース申込ステータスのオブジェクトが適切な内容を含み生成されていること() {
    Integer courseId = 888;

    CourseStatus courseStatus = sut.initializeCourseStatus(courseId);

    assertThat(courseStatus).isNotNull();
    assertThat(courseStatus.getCourseId()).isEqualTo(courseId);
    assertThat(courseStatus.getStatus()).isEqualTo("仮申込");
    assertThat(courseStatus.getProvisionalApplicationDate()).isEqualTo(LocalDate.now());
  }

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
    verify(repository, times(courseDetailList.size())).updateCourseStatus(
        any(CourseStatus.class));
  }

  //受講生更新処理：正常系(空コース詳細情報リストで更新）
  @Test
  void 受講生詳細情報の更新処理でコース詳細情報リストが空の場合に更新処理のrepositoryが実行されないこと()
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