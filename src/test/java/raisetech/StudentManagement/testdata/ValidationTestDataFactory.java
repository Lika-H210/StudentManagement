package raisetech.StudentManagement.testdata;

import java.time.LocalDate;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * Student および StudentCourse のバリデーションテスト用データを生成するユーティリティクラス。
 * 正常系のテストデータを基に引数で無効データを渡すことで一つの無効データを含むテストデータを生成できます。
 */
public class ValidationTestDataFactory {

  private final TestDataFactory baseFactory = new TestDataFactory();

  public Student withInvalidFullName(String invalidFullName) {
    Student student = baseFactory.createBaseStudent();
    student.setFullName(invalidFullName);
    return student;
  }

  public Student withInvalidAge(Integer invalidAge) {
    Student student = baseFactory.createBaseStudent();
    student.setAge(invalidAge);
    return student;
  }

  public Student withInvalidEmail(String invalidEmail) {
    Student student = baseFactory.createBaseStudent();
    student.setEmail(invalidEmail);
    return student;
  }

  public Student withInvalidSex(String invalidSex) {
    Student student = baseFactory.createBaseStudent();
    student.setSex(invalidSex);
    return student;
  }

  public StudentCourse withInvalidCourse(String invalidCourse) {
    StudentCourse studentCourse = baseFactory.createBaseCourse();
    studentCourse.setCourse(invalidCourse);
    return studentCourse;
  }

  public CourseStatus withInvalidStatus(String invalidStatus) {
    CourseStatus courseStatus = baseFactory.createBaseStatus();
    courseStatus.setStatus(invalidStatus);
    return courseStatus;
  }

  public CourseStatus withInvalidApplicationDate(LocalDate invalidApplicationDate) {
    CourseStatus courseStatus = baseFactory.createBaseStatus();
    courseStatus.setApplicationDate(invalidApplicationDate);
    return courseStatus;
  }

  public CourseStatus withInvalidCancelDate(LocalDate invalidCancelDate) {
    CourseStatus courseStatus = baseFactory.createBaseStatus();
    courseStatus.setCancelDate(invalidCancelDate);
    return courseStatus;
  }

}
