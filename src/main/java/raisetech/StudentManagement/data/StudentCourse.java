package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterCourse;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;
import raisetech.StudentManagement.view.JsonViews;

/**
 * Entity Class: 受講生 (`students_courses`) テーブルのエンティティクラス。 受講コース情報を管理し、データベースと対応しています。
 */
@Schema(description = "受講コース情報")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {

  @Schema(description = "コースID (主キー)", example = "101")
  @JsonView(JsonViews.OnAll.class)
  @Null(groups = {OnRegisterStudent.class, OnRegisterCourse.class},
      message = "登録時はコースIDは不要です。")
  @NotNull(groups = OnUpdate.class, message = "更新時はコースIDが必須です。")
  private Integer courseId;

  @Schema(description = "受講生ID (外部キー)", example = "1")
  @JsonView(JsonViews.OnAll.class)
  @Null(groups = OnRegisterStudent.class, message = "受講性情報と受講コース情報の同時登録時は受講生IDは不要です。")
  @NotNull(groups = {OnUpdate.class, OnRegisterCourse.class},
      message = "受講コース情報の登録及び更新時は受講生IDが必須です。")
  private Integer studentId;

  @Schema(description = "受講コース名", example = "English")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @NotEmpty(groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "コース名は必須です。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "コース名は100文字以内である必要があります。")
  private String course;

  @Schema(description = "受講開始日 (YYYY-MM-DD)", example = "2024-04-01")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  private LocalDate startDate;

  @Schema(description = "受講終了日 (未来または現在の日付)", example = "2025-03-31")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @FutureOrPresent(groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "受講終了日は未来または現在の日付を入力してください。")
  private LocalDate endDate;
}
