package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.criteria.StudentDetailSearchCriteria;

/**
 * Repository: 受講生 (`students`) および受講コース (`students_courses`) テーブルへのアクセスを提供するリポジトリ。
 */

@Mapper
public interface StudentRepository {

  /**
   * 指定条件を満たす受講生情報を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は検索対象外。
   *
   * @return 受講生の条件検索結果一覧(キャンセル扱いの受講生は対象外)
   */
  List<Student> searchStudentsByCriteria(StudentDetailSearchCriteria criteria);

  /**
   * 指定条件を満たす受講生情報を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は検索対象外。
   *
   * @return 受講生の条件検索結果一覧(キャンセル扱いの受講生は対象外)
   */
  List<StudentCourse> searchStudentsCoursesByCriteria(StudentDetailSearchCriteria criteria);

  /**
   * 指定条件を満たす受講生情報を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は検索対象外。
   *
   * @return 受講生の条件検索結果一覧(キャンセル扱いの受講生は対象外)
   */
  List<CourseStatus> searchCourseStatusesByCriteria(StudentDetailSearchCriteria criteria);

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
   * 指定された `courseId` に紐づく受講コース情報を取得します。
   *
   * @param courseIdList 受講生ID
   * @return zyy高コースIDと紐づく全ての受講コース情報を取得
   */
  List<CourseStatus> searchCourseStatusByCourseIdList(List<Integer> courseIdList);

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
   * 受講コース情報を新規登録します。 course_status_idは自動採番されます。
   *
   * @param courseStatus 登録する受講コース情報
   */
  void registerCourseStatus(CourseStatus courseStatus);

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

  /**
   * 指定された `courseId` の受講コース情報を更新します。
   *
   * @param CourseStatus 更新する受講コース情報
   */
  void updateCourseStatus(CourseStatus CourseStatus);

}
