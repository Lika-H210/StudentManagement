package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * Repository: 受講生 (`students`) および受講コース (`students_courses`) テーブルへのアクセスを提供するリポジトリ。
 */

@Mapper
public interface StudentRepository {

  /**
   * 受講生情報の一覧を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は除外されます。
   *
   * @return 受講生の一覧(キャンセル扱いの受講生を除く)
   */
  List<Student> searchStudents();

  /**
   * 受講コース情報の一覧を取得します。
   *
   * @return 受講コースの一覧(全件)
   */
  List<StudentCourse> searchStudentsCourses();

  /**
   * 指定された `studentId` に紐づく受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDと紐づく受講生情報を取得
   */
  Student searchStudentByStudentId(Integer studentId);

  /**
   * 指定された `studentId` に紐づく受講コース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDと紐づく全ての受講コース情報を取得
   */
  List<StudentCourse> searchStudentCoursesByStudentId(Integer studentId);

  /**
   * 受講生情報を新規登録します。 student_idは自動採番されます。
   *
   * @param student 登録する受講生情報
   */
  void registerStudent(Student student);

  /**
   * 受講コース情報を新規登録します。 course_idは自動採番されます。
   *
   * @param studentsCourses 登録する受講コース情報
   */
  void registerStudentCourse(StudentCourse studentsCourses);

  /**
   * 指定された `studentId` の受講生情報を更新します。
   *
   * @param student 更新する受講生情報
   */
  void updateStudent(Student student);

  /**
   * 指定された `courseId` の受講コース情報を更新します。
   *
   * @param studentCourse 更新する受講コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);
}
