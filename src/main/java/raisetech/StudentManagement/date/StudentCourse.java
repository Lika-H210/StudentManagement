package raisetech.StudentManagement.date;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class StudentCourse {

  //courseId必須 PRIKEY
  private Integer courseId;
  //studentId必須 FORKEY
  private Integer studentId;
  //course必須
  private String course;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

}
