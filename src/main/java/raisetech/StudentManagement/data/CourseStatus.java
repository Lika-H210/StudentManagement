package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
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
 * Entity Class: コース申込状況 (`courses_status`) テーブルのエンティティクラス。 受講コース情報の申込状況を管理し、
 */
@Schema(description = "コース申込状況")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourseStatus {

  @Schema(description = "コース申込状況ID (主キー)", example = "1")
  @JsonView(JsonViews.OnAll.class)
  @NotNull(groups = OnUpdate.class, message = "更新時はコース申込状況IDが必須です。")
  private Integer courseStatusId;

  @Schema(description = "コースID (外部キー)", example = "1")
  @JsonView(JsonViews.OnAll.class)
  @NotNull(groups = OnUpdate.class, message = "更新時はコースIDが必須です。")
  private Integer courseId;

  @Schema(description = "申込状況", example = "本申込")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @NotEmpty(groups = OnUpdate.class, message = "申込状況は必須です。")
  @Pattern(regexp = "仮申込|本申込|受講中|受講完了|キャンセル",
      groups = OnUpdate.class,
      message = "仮申込,本申込,受講中,受講完了,キャンセル のいずれかを入力してください")
  private String status;

  @Schema(description = "仮申込日", example = "2024-03-01")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @PastOrPresent(groups = OnUpdate.class,
      message = "過去または現在の日付を入力してください。")
  private LocalDate provisionalApplicationDate;

  @Schema(description = "本申込日", example = "2024-03-10")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @PastOrPresent(groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "過去または現在の日付を入力してください。")
  private LocalDate applicationDate;

  @Schema(description = "キャンセル日", example = "2024-03-20")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @PastOrPresent(groups = OnUpdate.class,
      message = "過去または現在の日付を入力してください。")
  private LocalDate cancelDate;

}

