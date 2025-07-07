package raisetech.StudentManagement.service.converter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void setUp() {
    sut = new StudentConverter();
  }

  //convertToStudentDetail:データの組み換え検証
  @Test
  void 受講生リストと受講コースリストからstudentIDに基づき適切なStudentDetailが返されること() {
    //前準備
    Student student777 = createStudentWithStudentId(777);
    Student student888 = createStudentWithStudentId(888);
    Student student999 = createStudentWithStudentId(999);

    StudentCourse student777Course1 = createCourseWithStudentId(777);
    StudentCourse student777Course2 = createCourseWithStudentId(777);
    StudentCourse student888Course1 = createCourseWithStudentId(888);
    StudentCourse noiseCourse = createCourseWithStudentId(1999);

    List<Student> studentList = List.of(student777, student888, student999);
    List<StudentCourse> studentCourseList = List.of(student777Course1, student777Course2,
        student888Course1, noiseCourse);

    //期待値の設定
    StudentDetail expectStudentDetail777 = new StudentDetail(student777,
        List.of(student777Course1, student777Course2));
    StudentDetail expectStudentDetail888 = new StudentDetail(student888,
        List.of(student888Course1));
    StudentDetail expectStudentDetail999 = new StudentDetail(student999, List.of());
    List<StudentDetail> expectStudentDetailList = List.of(expectStudentDetail777,
        expectStudentDetail888, expectStudentDetail999);

    //実行
    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, studentCourseList);

    //検証
    assertThat(actual.size()).isEqualTo(expectStudentDetailList.size());
    assertThat(actual).isEqualTo(expectStudentDetailList);
  }

  @Test
  void 受講コースリストが空の場合に受講生と空コースリストからなる受講生詳細が返されること() {
    Student student = createStudentWithStudentId(999);
    List<Student> studentList = List.of(student);
    List<StudentCourse> studentCourseList = List.of();

    List<StudentDetail> expectStudentDetailList = List.of(new StudentDetail(student, List.of()));

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, studentCourseList);

    assertThat(actual.size()).isEqualTo(expectStudentDetailList.size());
    assertThat(actual).isEqualTo(expectStudentDetailList);
  }

  @Test
  void 受講生リストが空の場合に空のリストが返されること() {
    List<Student> studentList = List.of();
    List<StudentCourse> studentCourseList = List.of(createCourseWithStudentId(999));

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, studentCourseList);

    assertThat(actual).isEmpty();
  }

  @Test
  void 受講生および受講コースの各リストが空の場合に空リストが返ること() {
    List<Student> studentList = List.of();
    List<StudentCourse> studentCourseList = List.of();

    List<StudentDetail> actual = sut.convertToStudentDetail(studentList, studentCourseList);

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

}