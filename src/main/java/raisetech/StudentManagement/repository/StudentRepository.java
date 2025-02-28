package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudents();

  /**
   * 受講コース情報の一覧を取得します。
   *
   * @return 受講コースの一覧(全件)
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentsCourses();

  /**
   * 指定された `studentId` に紐づく受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDと紐づく受講生情報を取得
   */
  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentByStudentId(Integer studentId);

  /**
   * 指定された `studentId` に紐づく受講コース情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生IDと紐づく全ての受講コース情報を取得
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCoursesByStudentId(Integer studentId);

  /**
   * 受講生情報を新規登録します。 student_idは自動採番されます。
   *
   * @param student 登録する受講生情報
   */
  @Insert(
      "INSERT INTO students (full_name, full_name_kana, nickname, mail_address, residence_area, age, sex, remark) "
          +
          "VALUES (#{fullName}, #{fullNameKana}, #{nickname}, #{mailAddress}, #{residenceArea}, #{age}, #{sex}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  /**
   * 受講コース情報を新規登録します。 course_idは自動採番されます。
   *
   * @param studentsCourses 登録する受講コース情報
   */
  @Insert(
      "INSERT INTO students_courses (student_id, course, start_date, end_date) "
          +
          "VALUES (#{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentsCourses);

  /**
   * 指定された `studentId` の受講生情報を更新します。
   *
   * @param student 更新する受講生情報
   */
  @Update("UPDATE students SET " +
      "full_name = #{fullName}, " +
      "full_name_kana = #{fullNameKana}, " +
      "nickname = #{nickname}, " +
      "mail_address = #{mailAddress}, " +
      "residence_area = #{residenceArea}, " +
      "age = #{age}, " +
      "sex = #{sex}, " +
      "remark = #{remark}, " +
      "is_deleted = #{isDeleted} " +
      "WHERE student_id = #{studentId}")
  void updateStudent(Student student);

  /**
   * 指定された `courseId` の受講コース情報を更新します。
   *
   * @param studentCourse 更新する受講コース情報
   */
  @Update("<script>" +
      "UPDATE students_courses " +
      "<set>" +
      "course = #{course}," +
      "<if test='startDate != null'>start_date = #{startDate},</if>" +
      "<if test='endDate != null'>end_date = #{endDate},</if>" +
      "</set> " +
      "WHERE course_id = #{courseId}" +
      "</script>")
  void updateStudentCourse(StudentCourse studentCourse);
}
