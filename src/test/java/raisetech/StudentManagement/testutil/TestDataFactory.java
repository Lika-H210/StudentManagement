package raisetech.StudentManagement.testutil;

import java.time.LocalDate;
import raisetech.StudentManagement.data.CourseStatus;
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

  //CourseStatus
  //OnRegisterとOnUpdateで必要に応じてcourseStatusIdとcourseIdを設定
  public static CourseStatus createInitCourseStatus(Integer courseStatusId,
      Integer courseId) {
    return CourseStatus.builder()
        .courseStatusId(courseStatusId)
        .courseId(courseId)
        .status("仮申込")
        .provisionalApplicationDate(LocalDate.now())
        .build();
  }

  public static CourseStatus createCourseStatusNormal(Integer courseStatusId, Integer courseId) {
    LocalDate provisionalApplicationDate = LocalDate.of(2025, 1, 1);
    return createInitCourseStatus(courseStatusId, courseId).toBuilder()
        .status("キャンセル")
        .provisionalApplicationDate(provisionalApplicationDate)
        .applicationDate(provisionalApplicationDate.plusDays(5))
        .cancelDate(provisionalApplicationDate.plusMonths(2))
        .build();
  }
}