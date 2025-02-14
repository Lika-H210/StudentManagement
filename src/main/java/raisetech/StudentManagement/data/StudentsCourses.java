package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentsCourses {

  //必須：courseId, studentId
  //studentId:StudentsのstudentIdと要整合(FOREIGN KEY)
  private Integer courseId;
  private Integer studentId;
  private String course;
  private LocalDate startDate;
  private LocalDate endDate;
}
