package raisetech.StudentManagement.testutil;

import java.time.LocalDate;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

public class TestDataFactory {

  //Student
  //OnRegisterとOnUpdateで必要に応じてstudentIdを設定
  public static Student createStudentNormalSimple(Integer studentId) {
    return Student.builder()
        .studentId(studentId)
        .fullName("森 一")
        .fullNameKana("モリ ハジメ")
        .build();
  }

  public static Student createStudentNormal(Integer studentId) {
    return createStudentNormalSimple(studentId).toBuilder()
        .nickname("もり")
        .mailAddress("mori.hajime@example.com")
        .residenceArea("東京都 足立区")
        .age(20)
        .sex("Male")
        .remark("特になし")
        .build();
  }

  //StudentCourse
  //OnRegisterとOnUpdateで必要に応じてcourseIdとstudentIdを設定
  public static StudentCourse createStudentCourseNormalSimple(Integer courseId, Integer studentId) {
    return StudentCourse.builder()
        .courseId(courseId)
        .studentId(studentId)
        .course("Java")
        .build();
  }

  public static StudentCourse createStudentCourseNormal(Integer courseId, Integer studentId) {
    return createStudentCourseNormalSimple(courseId, studentId).toBuilder()
        .startDate(LocalDate.now())
        .endDate(LocalDate.now().plusMonths(6))
        .build();
  }
}