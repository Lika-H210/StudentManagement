package raisetech.StudentManagement.controller.validation;

import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentCourseNormal;
import static raisetech.StudentManagement.testutil.TestDataFactory.createStudentNormal;

import java.time.LocalDate;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

public class ValidationTestDataFactory {

  public static Student createStudentForStudentIdValidTest(Integer studentId) {
    return createStudentNormal(studentId);
  }

  public static Student createStudentForFullNameValidTest(Integer studentId, String fullName) {
    Student student = createStudentNormal(studentId);
    student.setFullName(fullName);
    return student;
  }

  public static Student createStudentForMailValidTest(Integer studentId, String mailAddress) {
    Student student = createStudentNormal(studentId);
    student.setMailAddress(mailAddress);
    return student;
  }

  public static Student createStudentForSexValidTest(Integer studentId, String sex) {
    Student student = createStudentNormal(studentId);
    student.setSex(sex);
    return student;
  }

  public static StudentCourse createStudentCourseWithNoCourse(Integer courseId,
      Integer studentId) {
    StudentCourse studentCourse = createStudentCourseNormal(courseId, studentId);
    studentCourse.setCourse(null);
    return studentCourse;
  }

  public static StudentCourse createStudentCourseWithPastEndDate(Integer courseId,
      Integer studentId) {
    StudentCourse studentCourse = createStudentCourseNormal(courseId, studentId);
    studentCourse.setEndDate(LocalDate.of(2023, 12, 31));
    return studentCourse;
  }
}