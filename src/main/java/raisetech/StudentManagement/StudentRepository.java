package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.DeleteMapping;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> getStudentList();

  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name);

  @Insert("INSERT student values(#{name},#{age},#{course})")
  void registerStudent(String name, int age,String course);

  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updateStudentAge(String name, int age);

  @Update("UPDATE student SET course = #{course} WHERE name = #{name}")
  void updateStudentCourse(String name, String course);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);
}
