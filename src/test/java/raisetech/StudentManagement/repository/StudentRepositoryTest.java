package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormal;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.criteria.StudentDetailSearchCriteria;
import raisetech.StudentManagement.testutil.TestDataFactory;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  private StudentDetailSearchCriteria criteria;

  @BeforeEach
  void setUp() {
    criteria = new StudentDetailSearchCriteria();
  }

  // 条件指定受講生検索1
  @Test
  void 条件指定検索_検索条件に氏名を指定して想定通り検索できること() {
    criteria.setFullName("田");
    List<Student> actual = sut.searchStudentsByCriteria(criteria);

    assertThat(actual).hasSize(2);
    assertThat(actual).allMatch(student -> student.getFullName().contains("田"));
  }

  // 条件指定受講生検索2
  @Test
  void 条件指定受講生検索_検索条件にカナ名を指定して想定通り検索できること() {
    criteria.setFullNameKana("タロウ");
    List<Student> actual = sut.searchStudentsByCriteria(criteria);

    assertThat(actual).hasSize(2);
    assertThat(actual).allMatch(student -> student.getFullNameKana().contains("タロウ"));
  }

  // 条件指定受講生検索3
  @Test
  void 条件指定受講生検索_検索条件に居住地を指定して想定通り検索できること() {
    criteria.setResidenceArea("東京");
    List<Student> actual = sut.searchStudentsByCriteria(criteria);

    assertThat(actual).hasSize(2);
    assertThat(actual).allMatch(student -> student.getResidenceArea().contains("東京"));
  }

  // 条件指定受講生検索4
  @Test
  void 条件指定受講生検索_検索条件に前3条件を指定して想定通り検索できること() {
    criteria.setFullName("田");
    criteria.setFullNameKana("タロウ");
    criteria.setResidenceArea("東京");
    List<Student> actual = sut.searchStudentsByCriteria(criteria);

    assertThat(actual).hasSize(1);
    assertThat(actual).allMatch(student -> student.getResidenceArea().contains("東京"));
  }

  //　条件指定受講生検索5
  @Test
  void 条件指定受講生検索_条件未指定で論理削除を除く全件取得できること() {
    List<Student> actual = sut.searchStudentsByCriteria(criteria);
    boolean containsDeleted = actual.stream()
        .anyMatch(student -> student.getStudentId() == 4);
    assertThat(containsDeleted).isFalse();
  }

  //　条件指定受講生検索6
  @Test
  void 条件指定受講生検索_条件を満たす受講生がいない場合に空リストが返ること() {
    criteria.setFullName("佐々木");
    List<Student> actual = sut.searchStudentsByCriteria(criteria);
    assertThat(actual).isEmpty();
  }

  // 条件指定受講コース検索1
  @Test
  void 条件指定検索_検索条件にコース名を指定して想定通り検索できること() {
    criteria.setCourse("English");
    List<StudentCourse> actual = sut.searchStudentsCoursesByCriteria(criteria);

    assertThat(actual).hasSize(2);
    assertThat(actual).allMatch(student -> student.getCourse().contains("English"));
  }

  // 条件指定受講コース検索2
  @Test
  void 条件指定受講生検索_条件を満たす受講コースがいない場合に空リストが返ること() {
    criteria.setCourse("Chinese");
    List<StudentCourse> actual = sut.searchStudentsCoursesByCriteria(criteria);
    assertThat(actual).isEmpty();
  }

  // 条件指定コース申込状況検索1
  @Test
  void 条件指定検索_検索条件に申込状況を指定して想定通り検索できること() {
    criteria.setStatus("仮申込");
    List<CourseStatus> actual = sut.searchCourseStatusesByCriteria(criteria);

    assertThat(actual).hasSize(2);
    assertThat(actual).allMatch(student -> student.getStatus().contains("仮申込"));
  }

  // 条件指定コース申込状況検索2
  @Test
  void 条件指定受講生検索_条件を満たす申込状況がいない場合に空リストが返ること() {
    criteria.setStatus("Chinese");
    List<CourseStatus> actual = sut.searchCourseStatusesByCriteria(criteria);
    assertThat(actual).isEmpty();
  }

  //StudentIdに基づく受講生情報検索①
  @Test
  void studentIdに基づく受講生情報の検索ができること() {
    Student actual = sut.searchStudentByStudentId(2);
    assertThat(actual)
        .extracting(Student::getFullName, Student::getFullNameKana, Student::getMailAddress)
        .containsExactly("山田_花子", "ヤマダ_ハナコ", "yamada_hanako@gmail.com");
  }

  //StudentIdに基づく受講生情報検索②
  @Test
  void 存在しないstudentIdでは受講生情報としてnullが返ること() {
    Student actual = sut.searchStudentByStudentId(999); // 存在しないID
    assertThat(actual).isNull();
  }

  //StudentIdに基づく受講コース情報検索①
  @Test
  void studentIdに基づく受講コース情報の検索ができること() {
    List<StudentCourse> actual = sut.searchStudentCoursesByStudentId(2);
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .extracting(StudentCourse::getCourse)
        .containsExactlyInAnyOrder("Japanese", "French");
  }

  //StudentIdに基づく受講コース情報検索②
  @Test
  void 存在しないstudentIdでは受講コース情報が0件になること() {
    List<StudentCourse> actual = sut.searchStudentCoursesByStudentId(999);
    assertThat(actual).isEmpty();
  }

  //CourseIdに紐づく受講コース情報検索①
  @Test
  void courseIdのリストに紐づけられるcourse_statusが検索できること() {
    List<CourseStatus> actual = sut.searchCourseStatusByCourseIdList(List.of(1, 3, 5));
    assertThat(actual.size()).isEqualTo(3);
    assertThat(actual)
        .extracting(CourseStatus::getCourseId)
        .containsExactlyInAnyOrder(1, 3, 5);
  }

  //CourseIdに紐づく受講コース情報検索②
  @Test
  void courseIdのリストに存在しないIDを指定した場合はcourse_statusが0件になること() {
    List<CourseStatus> actual = sut.searchCourseStatusByCourseIdList(List.of(999, 1000));
    assertThat(actual).isEmpty();
  }

  //受講生登録処理
  @Test
  void 受講生情報の登録が行えること() {
    Student student = createStudentNormal(null);

    sut.registerStudent(student);
    //studentIDの発行確認
    assertThat(student.getStudentId()).isNotNull();

    // IDが割り当てられたことを前提に検索・内容を検証
    Student actual = sut.searchStudentByStudentId(student.getStudentId());

    assertThat(actual)
        .extracting(
            Student::getFullName,
            Student::getFullNameKana,
            Student::getMailAddress,
            Student::getAge,
            Student::isDeleted
        )
        .containsExactly("森 一", "モリ ハジメ", "mori.hajime@example.com", 20, false);
  }

  //受講コース登録処理
  @Test
  void 受講コースの登録が行えること() {
    StudentCourse studentCourse = createStudentCourseNormal(null, 2);

    sut.registerStudentCourse(studentCourse);
    //courseIDの発行確認
    assertThat(studentCourse.getCourseId()).isNotNull();

    // IDが割り当てられたことを前提に検索・内容を検証
    StudentCourse actual = sut.searchStudentCoursesByStudentId(2).stream()
        .filter(sc -> sc.getCourseId().equals(studentCourse.getCourseId()))
        .findFirst()
        .orElseThrow();

    assertThat(actual)
        .extracting(
            StudentCourse::getStudentId,
            StudentCourse::getCourse
        )
        .containsExactly(2, "Java");
  }

  @Test
  void コース申込状況を新規登録できること() {
    Integer courseId = 7;
    LocalDate today = LocalDate.now();
    CourseStatus courseStatus = TestDataFactory.createInitCourseStatus(null, courseId);

    sut.registerCourseStatus(courseStatus);

    // courseStatusIdの発行確認
    assertThat(courseStatus.getCourseStatusId()).isNotNull();

    // IDが割り当てられたことを前提に検索・内容を検証
    List<CourseStatus> actualList = sut.searchCourseStatusByCourseIdList(List.of(courseId));
    assertThat(actualList)
        .hasSize(1)
        .first()
        .extracting(
            CourseStatus::getCourseId,
            CourseStatus::getStatus,
            CourseStatus::getProvisionalApplicationDate
        )
        .containsExactly(courseId, "仮申込", today);
  }

  //受講生更新処理
  @Test
  void 受講生情報の更新が行えること() {
    Student student = sut.searchStudentByStudentId(2);
    student.setNickname(null);
    student.setMailAddress("new_mail@example.com");
    student.setDeleted(true);

    sut.updateStudent(student);

    Student actual = sut.searchStudentByStudentId(2);

    assertThat(actual)
        .extracting(
            Student::getStudentId,
            Student::getFullName,
            Student::getNickname,
            Student::getMailAddress,
            Student::isDeleted
        )
        .containsExactly(2, "山田_花子", null, "new_mail@example.com", true);
  }

  //受講コース更新処理
  @Test
  void 受講コース情報の更新が行えること() {
    StudentCourse course = sut.searchStudentCoursesByStudentId(2).get(1);
    course.setCourse("Chinese");
    course.setEndDate(course.getEndDate().plusMonths(1));

    sut.updateStudentCourse(course);

    StudentCourse actual = sut.searchStudentCoursesByStudentId(2).stream()
        .filter(sc -> sc.getCourseId().equals(course.getCourseId()))
        .findFirst()
        .orElseThrow();

    assertThat(actual)
        .extracting(
            StudentCourse::getStudentId,
            StudentCourse::getCourse,
            StudentCourse::getEndDate
        )
        .containsExactly(2, "Chinese", course.getEndDate());
  }

  @Test
  void 受講コース申込状況の更新が行えること() {
    List<CourseStatus> courseStatusList = sut.searchCourseStatusByCourseIdList(List.of(2));
    assertThat(courseStatusList).hasSize(1);
    CourseStatus courseStatus = courseStatusList.getFirst();

    courseStatus.setStatus("受講完了");
    courseStatus.setCancelDate(LocalDate.of(2025, 4, 15));
    courseStatus.setApplicationDate(LocalDate.of(2025, 1, 15));

    sut.updateCourseStatus(courseStatus);

    // 更新後のデータを再取得して検証
    List<CourseStatus> updatedList = sut.searchCourseStatusByCourseIdList(List.of(2));
    assertThat(updatedList).hasSize(1);
    CourseStatus actual = updatedList.getFirst();

    assertThat(actual)
        .extracting(
            CourseStatus::getCourseId,
            CourseStatus::getStatus,
            CourseStatus::getApplicationDate,
            CourseStatus::getCancelDate
        )
        .containsExactly(
            courseStatus.getCourseId(),
            "受講完了",
            LocalDate.of(2025, 1, 15),
            LocalDate.of(2025, 4, 15)
        );
  }
}