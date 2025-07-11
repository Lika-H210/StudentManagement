package raisetech.StudentManagement.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void setUp() {
    sut = new StudentConverter();
  }

  //convertToStudentDetail:データの組み換え検証
  @Test
  void 受講生リストと受講コース詳細リストからstudentIDに基づき適切なStudentDetailが返されること() {
    //前準備
    Student student777 = createStudentWithStudentId(777);
    Student student888 = createStudentWithStudentId(888);
    Student student999 = createStudentWithStudentId(999);

    StudentCourse student777Course1 = createCourseWithStudentId(777);
    StudentCourse student777Course2 = createCourseWithStudentId(777);
    StudentCourse student888Course1 = createCourseWithStudentId(888);
    StudentCourse noiseCourse = createCourseWithStudentId(1999);

    CourseDetail courseDetail777Course1 = new CourseDetail(student777Course1, new CourseStatus());
    CourseDetail courseDetail777Course2 = new CourseDetail(student777Course2, new CourseStatus());
    CourseDetail courseDetail888Course1 = new CourseDetail(student888Course1, new CourseStatus());
    CourseDetail courseDetailNoiseCourse = new CourseDetail(noiseCourse, new CourseStatus());

    List<Student> studentList = List.of(student777, student888, student999);
    List<CourseDetail> courseDetailList = List.of(courseDetail777Course1, courseDetail777Course2,
        courseDetail888Course1, courseDetailNoiseCourse);

    //期待値の設定
    StudentDetail expectStudentDetail777 = new StudentDetail(student777,
        List.of(courseDetail777Course1, courseDetail777Course2));
    StudentDetail expectStudentDetail888 = new StudentDetail(student888,
        List.of(courseDetail888Course1));
    StudentDetail expectStudentDetail999 = new StudentDetail(student999, List.of());
    List<StudentDetail> expectStudentDetailList = List.of(expectStudentDetail777,
        expectStudentDetail888, expectStudentDetail999);

    //実行
    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, courseDetailList);

    //検証
    assertThat(actual.size()).isEqualTo(expectStudentDetailList.size());
    assertThat(actual).isEqualTo(expectStudentDetailList);
  }

  @Test
  void 受講コース詳細情報のリストが空の場合に受講生と空コースリストからなる受講生詳細が返されること() {
    Student student = createStudentWithStudentId(999);
    List<Student> studentList = List.of(student);
    List<CourseDetail> courseDetailList = List.of();

    List<StudentDetail> expectStudentDetailList = List.of(new StudentDetail(student, List.of()));

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, courseDetailList);

    assertThat(actual.size()).isEqualTo(expectStudentDetailList.size());
    assertThat(actual).isEqualTo(expectStudentDetailList);
  }

  @Test
  void 受講生リストが空の場合に空のリストが返されること() {
    List<Student> studentList = List.of();
    List<CourseDetail> courseDetailList = List.of(
        new CourseDetail(createCourseWithStudentId(999), new CourseStatus()));

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, courseDetailList);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生および受講コース詳細情報の各リストが空の場合に空リストが返ること() {
    List<Student> studentList = List.of();
    List<CourseDetail> courseDetailList = List.of();

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, courseDetailList);

    assertThat(actual).isEmpty();
  }

  //convertToCourseDetail:データの組み換え検証
  @Test
  void 受講コースとコース申込ステータスのリストから両リストに同じcourseID持つレコードのみをCourseDetailに変換しリスト化して返されること() {
    //前準備
    StudentCourse studentCourse888 = createCourseWithCourseId(888);
    StudentCourse studentCourse999 = createCourseWithCourseId(999);

    CourseStatus courseStatus777 = createStatusWithCourseId(777);
    CourseStatus courseStatus888 = createStatusWithCourseId(888);

    List<StudentCourse> studentCourseList = List.of(studentCourse888, studentCourse999);
    List<CourseStatus> courseStatusList = List.of(courseStatus777, courseStatus888);

    //期待値の設定
    CourseDetail expectCourseDetail888 = new CourseDetail(studentCourse888, courseStatus888);
    List<CourseDetail> expectCourseDetailList = List.of(expectCourseDetail888);

    //実行
    List<CourseDetail> actual = sut.convertToCourseDetail(studentCourseList, courseStatusList);

    //検証
    assertThat(actual.size()).isEqualTo(expectCourseDetailList.size());
    assertThat(actual).isEqualTo(expectCourseDetailList);
  }

  @Test
  void 受講コース情報のリストが空の場合に空のコース詳細情報が返されること() {
    List<StudentCourse> studentCourseList = List.of();
    List<CourseStatus> courseStatusList = List.of(createStatusWithCourseId(999));

    List<CourseDetail> actual = sut.convertToCourseDetail(studentCourseList, courseStatusList);

    assertThat(actual).isEmpty();
  }

  @Test
  void コース申込ステータス情報のリストが空の場合に空のコース詳細情報が返されること() {
    List<StudentCourse> studentCourseList = List.of(createCourseWithCourseId(999));
    List<CourseStatus> courseStatusList = List.of();

    List<CourseDetail> actual = sut.convertToCourseDetail(studentCourseList, courseStatusList);

    assertThat(actual).isEmpty();
  }

  //受講生IDのみを設定したStudentオブジェクトを生成する
  private Student createStudentWithStudentId(Integer studentId) {
    Student student = new Student();
    student.setStudentId(studentId);
    return student;
  }

  //受講生IDのみを設定したStudentCourseオブジェクトを生成する
  private StudentCourse createCourseWithStudentId(Integer studentId) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(studentId);
    return studentCourse;
  }

  //コースIDのみを設定したStudentCourseオブジェクトを生成する
  private StudentCourse createCourseWithCourseId(Integer courseId) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(courseId);
    return studentCourse;
  }

  //コースIDのみを設定したCourseStatusオブジェクトを生成する
  private CourseStatus createStatusWithCourseId(Integer courseId) {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setCourseId(courseId);
    return courseStatus;
  }

}