package raisetech.StudentManagement;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  //courseId必須 PRIKEY
  private Integer courseId;
  //studentId必須 FORKEY
  private Integer studentId;
  //course必須
  private String course;
  private LocalDate startDate;
  private LocalDate endDate;

}
