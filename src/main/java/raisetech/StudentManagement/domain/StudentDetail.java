package raisetech.StudentManagement.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentCourseList;

}
