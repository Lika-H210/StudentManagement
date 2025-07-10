package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講コース情報を表すエンティティクラスです。DBのstudents_courseテーブルに対応します。
 */
@Schema(description = "受講コース情報")
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {

  @Schema(description = "コースID", example = "1")
  @JsonView(RequestViews.Update.class)
  @Null(groups = RegisterGroup.class, message = "登録時はコースIDは不要です")
  @NotNull(groups = UpdateGroup.class, message = "更新時はコースIDは必須です")
  private Integer courseId;

  @Schema(description = "受講生ID", example = "1")
  @JsonView(RequestViews.Update.class)
  @Null(groups = RegisterGroup.class, message = "登録時は受講生IDは不要です")
  private Integer studentId;

  @Schema(description = "コース名", example = "AWSコース")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "コース名の入力は必須です")
  @Size(max = 30, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "コース名は30文字以内で入力してください")
  private String course;

  @Schema(description = "受講開始日", example = "2025-01-01")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  private LocalDate startDate;

  @Schema(description = "受講終了日", example = "2025-06-30")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  private LocalDate endDate;

}
