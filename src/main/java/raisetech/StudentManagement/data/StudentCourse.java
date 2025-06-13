package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;

/**
 * 受講コース情報を表すエンティティクラスです。DBのstudents_courseテーブルに対応します。
 */
@Getter
@Setter
@Validated
public class StudentCourse {

  //courseId必須 PRIKEY
  @Null(groups = RegisterGroup.class, message = "登録時はコースIDは不要です")
  @NotNull(groups = UpdateGroup.class, message = "更新時はコースIDは必須です")
  private Integer courseId;

  //studentId必須 FORKEY
  @Null(groups = RegisterGroup.class, message = "登録時は受講生IDは不要です")
  private Integer studentId;

  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "コース名の入力は必須です")
  @Size(max = 30, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "コース名は30文字以内で入力してください")
  private String course;

  private LocalDate startDate;

  private LocalDate endDate;

}
