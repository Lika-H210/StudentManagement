package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.condition.SearchCondition;

/**
 * 受講生情報および受講コース情報及びコース申込ステータスに関するDBアクセス処理を定義するリポジトリインターフェースです。
 * 対象テーブル：students、students_courses、course_status
 */
@Mapper
public interface StudentRepository {

  /**
   * 検索条件に合致する受講生の情報を取得します。(但し、キャンセル扱い(isDeleted=true)の受講生は除きます）
   *
   * @param condition 検索条件
   * @return 受講生情報（Student）のリスト
   */
  List<Student> searchStudentList(SearchCondition condition);

  /**
   * 検索条件に合致する受講コース情報を取得します。
   *
   * @param condition 検索条件
   * @return 受講コース情報（StudentCourse）のリスト
   */
  List<StudentCourse> searchStudentCourseList(SearchCondition condition);

  /**
   * 検索条件に合致するコース申込ステータス情報を取得します。
   *
   * @param condition 検索条件
   * @return コース申込スタータス情報（CourseStatus）のリスト
   */
  List<CourseStatus> searchCourseStatusList(SearchCondition condition);

  /**
   * publicIdに対応した受講生情報を取得します。
   *
   * @param publicId 受講生の Public ID（UUID）
   * @return 対象の受講生情報（Student）
   */
  Student searchStudentByPublicId(String publicId);

  /**
   * 指定された受講生IDに関連するすべての受講コース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 対象受講生の受講コース情報（StudentCourse）のリスト
   */
  List<StudentCourse> searchStudentCourseListByStudentId(Integer studentId);

  /**
   * 指定されたコースIDと紐づくコース申込ステータス情報を取得します。
   *
   * @param courseIdList 検索対象のcourseIDのリスト
   * @return 対象コースIDに紐づくコース申込ステータス（CourseStatus）のリスト
   */
  List<CourseStatus> searchCourseStatusListByCourseIdList(List<Integer> courseIdList);

  /**
   * 受講生情報を新規登録します。主キー（studentId）は自動生成され、登録後にオブジェクトにセットされます。
   *
   * @param student 登録する受講生情報
   */
  void registerStudent(Student student);

  /**
   * 受講コース情報を新規登録します。主キー（courseId）は自動生成され、登録後にオブジェクトにセットされます。
   *
   * @param studentCourse 登録する受講コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * コース申込ステータスを新規登録します。主キー（statusId）は自動生成され、登録後にオブジェクトにセットされます。
   *
   * @param courseStatus 登録するコース申込ステータス情報
   */
  void registerCourseStatus(CourseStatus courseStatus);

  /**
   * publicIdに対応した受講生情報を更新します。
   *
   * @param student 更新対象の受講生情報
   */
  void updateStudent(Student student);

  /**
   * courseIdに対応した受講コース情報を更新します。
   *
   * @param studentCourse 更新対象の受講コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * courseIdに対応したコース申込ステータスを更新します。
   *
   * @param courseStatus 更新対象のコース申込ステータス
   */
  void updateCourseStatus(CourseStatus courseStatus);

  /**
   * 指定されたメールアドレスが既に登録されているかを確認します。
   *
   * @param email 受講生のメールアドレス
   * @return true 登録済みの場合、false 未登録の場合
   */
  boolean existsByEmail(String email);

  /**
   * 指定されたメールアドレスが既に登録されているかを確認します。
   *
   * @param publicId 公開用ID
   * @param email    受講生のメールアドレス
   * @return true 登録済みの場合、false 未登録の場合
   */
  boolean existsByEmailExcludingPublicId(@Param("publicId") String publicId,
      @Param("email") String email);
}
