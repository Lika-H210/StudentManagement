package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
  void registerStudent(String name, Integer age,String course);

  @Update("<script>"
      + "UPDATE student "
      + "<set>"
      + "  <if test='age != null'> age = #{age}, </if>"
      + "  <if test='course != null'> course = #{course}, </if>"
      + "</set>"
      + "WHERE name = #{name}"
      + "</script>")
  void updateStudent(@Param("name") String name,
      @Param("age") Integer age,
      @Param("course") String course);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);
}
