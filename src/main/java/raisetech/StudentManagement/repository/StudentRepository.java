package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ:
 */

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudents();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentsByStudentId(Integer studentId);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentsCourses> searchStudentsCoursesByStudentId(Integer studentId);

  @Insert(
      "INSERT INTO students (student_id, full_name, full_name_kana, nickname, mail_address, residence_area, age, sex, remark) "
          +
          "VALUES (#{studentId}, #{fullName}, #{fullNameKana}, #{nickname}, #{mailAddress}, #{residenceArea}, #{age}, #{sex}, #{remark})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert(
      "INSERT INTO students_courses (course_id, student_id, course, start_date, end_date) "
          +
          "VALUES (#{courseId}, #{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentsCourses studentsCourses);

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

  @Update("<script>" +
      "UPDATE students_courses " +
      "<set>" +
      "course = #{course}," +
      "<if test='startDate != null'>start_date = #{startDate},</if>" +
      "<if test='endDate != null'>end_date = #{endDate},</if>" +
      "</set> " +
      "WHERE course_id = #{courseId}" +
      "</script>")
  void updateStudentCourse(StudentsCourses course);
}
