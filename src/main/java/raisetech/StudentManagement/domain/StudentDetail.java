package raisetech.StudentManagement.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;

/**
 * 受講生情報（Student）と受講生に紐づく受講コース情報（StudentCourse）をまとめた受講生詳細情報クラスです。 主に画面表示やデータ入出力時のDTOとして使用されます。
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class StudentDetail {

  @NotNull(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "受講生情報の入力は必須です。")
  @Valid
  private Student student;

  @NotNull(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "登録に必要な情報が不足しています。システム管理者にご連絡ください。")
  @Valid
  private List<StudentCourse> studentCourseList;

}
