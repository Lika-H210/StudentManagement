package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudentList();

  //todo:引数をpublicIdに変更する
  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentById(Integer studentId);

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

}
