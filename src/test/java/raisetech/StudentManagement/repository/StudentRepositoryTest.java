package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormal;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.testutil.TestDataFactory;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  //受講生情報一覧検索①
  @Test
  void キャンセル扱いを除いた受講生情報の全件を検索できること() {
    List<Student> actual = sut.searchStudents();
    assertThat(actual.size()).isEqualTo(4);
  }

  //受講生情報一覧検索②
  @Test
  void キャンセル扱いの受講生は検索結果に含まれないこと() {
    List<Student> actual = sut.searchStudents();
    boolean containsDeleted = actual.stream()
        .anyMatch(student -> student.getStudentId() == 4);
    assertThat(containsDeleted).isFalse();
  }

  //受講コース情報一覧検索
  @Test
  void 受講コース情報の全件を検索できること() {
    List<StudentCourse> actual = sut.searchStudentsCourses();
    assertThat(actual.size()).isEqualTo(6);
  }

  //受講コース情報一覧検索
  @Test
  void 全件取得でcourse_statusが全て取得できること() {
    List<CourseStatus> actual = sut.searchCoursesStatus();
    assertThat(actual.size()).isEqualTo(6);
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
    LocalDate today = LocalDate.now();
    CourseStatus courseStatus = TestDataFactory.createInitCourseStatus(null, 2);

    sut.registerCourseStatus(courseStatus);

    // courseStatusIdの発行確認
    assertThat(courseStatus.getCourseStatusId()).isNotNull();

    // IDが割り当てられたことを前提に検索・内容を検証
    CourseStatus actual = sut.searchCoursesStatus().stream()
        .filter(cs -> cs.getCourseStatusId().equals(courseStatus.getCourseStatusId()))
        .findFirst()
        .orElseThrow();

    assertThat(actual)
        .extracting(
            CourseStatus::getCourseId,
            CourseStatus::getStatus,
            CourseStatus::getProvisionalApplicationDate
        )
        .containsExactly(2, "仮申込", today);
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
    CourseStatus courseStatus = sut.searchCoursesStatus().stream()
        .filter(cs -> cs.getCourseStatusId().equals(2))
        .findFirst()
        .orElseThrow();

    courseStatus.setStatus("受講完了");
    courseStatus.setCancelDate(LocalDate.of(2025, 4, 15));
    courseStatus.setApplicationDate(LocalDate.of(2025, 1, 15));

    sut.updateCourseStatus(courseStatus);

    // 更新後のデータを再取得して検証
    CourseStatus actual = sut.searchCoursesStatus().stream()
        .filter(cs -> cs.getCourseStatusId().equals(2))
        .findFirst()
        .orElseThrow();

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