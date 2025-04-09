package raisetech.StudentManagement.controller;

import java.time.LocalDate;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

class TestDataFactory {

  //Student
  //OnRegisterとOnUpdateで必要に応じてstudentIdを設定
  public static Student createStudentNormal(Integer studentId) {
    Student student = new Student();
    student.setStudentId(studentId);
    student.setFullName("森 一");
    student.setFullNameKana("モリ ハジメ");
    student.setNickname("もり");
    student.setMailAddress("mori.hajime@example.com");
    student.setResidenceArea("東京都 足立区");
    student.setAge(20);
    student.setSex("Male");
    student.setRemark("特になし");
    return student;
  }

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

  //StudentCourse
  //OnRegisterとOnUpdateで必要に応じてcourseIdとstudentIdを設定
  public static StudentCourse createStudentCourseNormal(Integer courseId, Integer studentId) {
    StudentCourse course = new StudentCourse();
    course.setCourseId(courseId);
    course.setStudentId(studentId);
    course.setCourse("Java");
    course.setStartDate(LocalDate.now());
    course.setEndDate(LocalDate.now().plusMonths(6));
    return course;
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

//  public static StudentDetail createValidStudentDetail() {
//    return new StudentDetail(createValidStudent(), List.of(createValidStudentCourse()));
//  }

}