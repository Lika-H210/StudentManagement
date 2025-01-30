package raisetech.StudentManagement.repository;

import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;

/**
 * 受講生情報を扱うリポジトリ:
 */

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  @Select("SELECT * FROM students_courses")
  List<StudentsCourses> searchStudentsCourses();

  @Insert(
      "INSERT INTO students (student_id, full_name, full_name_kana, nickname, mail_address, residence_area, age, sex, remark) "
          +
          "VALUES (#{studentId}, #{fullName}, #{fullNameKana}, #{nickname}, #{mailAddress}, #{residenceArea}, #{age}, #{sex}, #{remark})")
  void registerStudent(String studentId, String fullName, String fullNameKana, String nickname,
      String mailAddress, String residenceArea, Integer age, String sex, String remark);

  @Insert(
      "INSERT INTO students_courses (course_id, student_id, course, start_date, end_date) "
          +
          "VALUES (#{courseId}, #{studentId}, #{course}, #{startDate}, #{endDate})")
  void registerStudentCourse(String courseId, String studentId, String course,
      LocalDate startDate, LocalDate endDate);


}
