package raisetech.StudentManagement.domain;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講コース情報（StudentCourse）と受講コースに紐づく申込ステータス（CourseStatus）をまとめた受講コース詳細情報クラスです。
 */
@Schema(description = "受講コース詳細情報")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class CourseDetail {

  @Schema(description = "受講コース情報")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotNull(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "受講コース情報の入力は必須です")
  @Valid
  private StudentCourse studentCourse;

  @Schema(description = "コース申込ステータス（登録時は自動生成されるためリクエストは不要です）")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Null(groups = RegisterGroup.class, message = "登録時コース申込ステータスは不要です")
  @NotNull(groups = UpdateGroup.class, message = "更新時コース申込ステータスは必須です")
  @Valid
  private CourseStatus courseStatus;

}
