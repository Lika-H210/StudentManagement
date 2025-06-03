package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudentList();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  //todo:引数をpublicIdに変更する
  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentById(Integer studentId);

  @Insert(
      "INSERT INTO students (public_id, full_name, kana_name, nickname, email, region, age, sex, remark, is_deleted)"
          + "VALUES (#{publicId}, #{fullName}, #{kanaName}, #{nickname}, #{email}, #{region}, #{age}, #{sex}, #{remark}, #{isDeleted})")
  void registerStudent(Student student);

}
