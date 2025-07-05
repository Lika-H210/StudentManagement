package raisetech.StudentManagement.testdata;

import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * Student および StudentCourse のテスト用データを生成するユーティリティクラス。 このクラスの定数値はバリデーションを通過する値となっています。
 */
public class TestDataFactory {

  private static final Integer STUDENT_ID = 999;
  private static final String PUBLIC_ID = "00000000-0000-0000-0000-000000000000";
  private static final String FULL_NAME = "テスト 太郎";
  private static final String KANA_NAME = "テスト タロウ";
  private static final String EMAIL = "test@example.com";

  private static final Integer COURSE_ID = 888;
  private static final String COURSE = "XXXコース";

  /**
   * DB上の最低限の必須項目からなるStudentのインスタンスを生成する
   *
   * @return student
   */
  public Student createBaseStudent() {
    Student student = new Student();
    student.setStudentId(STUDENT_ID);
    student.setPublicId(PUBLIC_ID);
    student.setFullName(FULL_NAME);
    student.setKanaName(KANA_NAME);
    student.setEmail(EMAIL);
    return student;
  }

  /**
   * 登録処理時の最低限の必須項目からなるStudentのインスタンスを生成する(ID情報は含まない)
   *
   * @return student
   */
  public Student createBaseStudentForRegister() {
    Student student = new Student();
    student.setFullName(FULL_NAME);
    student.setKanaName(KANA_NAME);
    student.setEmail(EMAIL);
    return student;
  }

  /**
   * DB上の最低限の必須項目からなるStudentCourseのインスタンスを生成する
   *
   * @return student
   */
  public StudentCourse createBaseCourse() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId(COURSE_ID);
    studentCourse.setStudentId(STUDENT_ID);
    studentCourse.setCourse(COURSE);
    return studentCourse;
  }

  /**
   * 登録処理時の最低限の必須項目からなるStudentCourseのインスタンスを生成する(ID情報は含まない)
   *
   * @return student
   */
  public StudentCourse createBaseCourseForRegister() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourse(COURSE);
    return studentCourse;
  }
}
