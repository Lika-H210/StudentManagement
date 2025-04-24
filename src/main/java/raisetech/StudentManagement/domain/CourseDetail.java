package raisetech.StudentManagement.domain;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;
import raisetech.StudentManagement.view.JsonViews;


/**
 * domain:受講コース詳細情報を表すクラスです。 受講コース情報 (`StudentCourse`) と受講コース申込み状況 (`CourseStatus`) で構成されます。
 */
@Schema(description = "受講コース詳細情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetail {

  @Schema(description = "受講コース情報", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Valid
  @NotNull(groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "受講コース情報は必須です。")
  private StudentCourse studentCourse;

  @Schema(description = "コース申込み状況")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Null(groups = OnRegisterStudent.class, message = "コース申込み状況は不要です。")
  @NotNull(groups = OnUpdate.class, message = "コース申込み状況は必須です。")
  @Valid
  private CourseStatus courseStatus;
}
