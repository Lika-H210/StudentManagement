package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> searchStudentList();

  @Select("SELECT * FROM student WHERE student_id = #{studentId}")
  Student searchStudentById(String studentId);

  @Insert("INSERT INTO student VALUES (#{studentId}, #{name}, #{kanaName}, #{age})")
  void registerStudent(String studentId, String name, String kanaName, Integer age);

  @Insert("INSERT INTO student VALUES (#{studentId}, #{name}, #{kanaName}, #{age})")
  void registerStudentByStudent(Student student);

  @Update("UPDATE student SET name = #{name}, kana_name = #{kanaName}, age =#{age} WHERE student_id = #{studentId}")
  void putUpdateStudent(String studentId, String name, String kanaName, Integer age);

  @Update({
      "<script>",
      "UPDATE student",
      "<set>",
      "<if test='name != null'> name = #{name} </if>",
      "<if test='kanaName != null'> kana_name = #{kanaName} </if>",
      "<if test='age != null'> age =#{age} </if>",
      "</set>",
      "WHERE student_id = #{studentId}",
      "</script>"
  })
  void patchUpdateStudent(String studentId, String name, String kanaName, Integer age);

  @Delete("DELETE FROM student WHERE student_id = #{studentId}")
  void deleteStudent(String studentId);
}
