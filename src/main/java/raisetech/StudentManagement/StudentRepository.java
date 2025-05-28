package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> searchStudentList();

  @Select("SELECT * FROM student WHERE student_id = #{studentId}")
  Student searchStudentById(Integer studentId);

}
