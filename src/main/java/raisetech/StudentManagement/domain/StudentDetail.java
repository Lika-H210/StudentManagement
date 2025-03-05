package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * domain:受講生詳細情報を表すクラスです。 受講生情報 (`Student`) と受講コース情報 (`StudentCourse`) で構成されます。
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  @NotNull(message = "受講生情報は必須です。")
  private Student student;

  @Valid
  private List<StudentCourse> studentCourseList;

}

