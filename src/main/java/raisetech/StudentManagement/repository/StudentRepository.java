package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
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
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert(
      "INSERT INTO students_courses (course_id, student_id, course, start_date, end_date) "
          +
          "VALUES (#{courseId}, #{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentsCourses studentsCourses);
}
