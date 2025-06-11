package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students WHERE is_deleted = false")
  List<Student> searchStudentList();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  @Select("SELECT * FROM students WHERE public_id = #{publicId}")
  Student searchStudentByPublicId(String publicId);

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCourseListByStudentId(Integer studentId);

  @Insert(
      "INSERT INTO students (public_id, full_name, kana_name, nickname, email, region, age, sex, remark, is_deleted)"
          + "VALUES (#{publicId}, #{fullName}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{sex}, #{remark}, #{isDeleted})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course, start_date, end_date)"
      + "VALUES (#{studentId}, #{course}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  @Update("UPDATE students "
      + "SET full_name = #{fullName}, kana_name = #{kanaName}, nickname = #{nickname}, email = #{email}, "
      + "region = #{region}, age = #{age}, sex = #{sex}, remark = #{remark}, is_deleted = #{isDeleted} "
      + "WHERE public_id = #{publicId}")
  void updateStudent(Student student);

  @Update("UPDATE students_courses "
      + "SET course = #{course}, start_date = #{startDate}, end_date = #{endDate} "
      + "WHERE course_id = #{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

}
