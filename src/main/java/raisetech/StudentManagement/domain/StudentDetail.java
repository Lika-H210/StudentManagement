package raisetech.StudentManagement.domain;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講生情報（Student）と受講生に紐づく受講コース詳細情報（CourseDetail）をまとめた受講生詳細情報クラスです。 主に画面表示やデータ入出力時のDTOとして使用されます。
 */
@Schema(description = "受講生詳細情報")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class StudentDetail {

  @Schema(description = "受講生情報")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotNull(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "受講生情報の入力は必須です。")
  @Valid
  private Student student;

  @Schema(description = "受講コース詳細情報")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotNull(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "登録・更新処理に必要な情報が不足しています。システム管理者にご連絡ください。")
  @Valid
  private List<CourseDetail> courseDetailList;

}
