package raisetech.StudentManagement.domain;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;
import raisetech.StudentManagement.view.JsonViews;

/**
 * domain:受講生詳細情報を表すクラスです。 受講生情報 (`Student`) と受講コース情報 (`StudentCourse`) で構成されます。
 */
@Schema(description = "受講生詳細情報")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDetail {

  @Schema(description = "受講生の基本情報", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Valid
  @NotNull(groups = {OnRegisterStudent.class, OnUpdate.class}, message = "受講生情報は必須です。")
  private Student student;

  @Schema(description = "受講コース詳細情報のリスト")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Valid
  private List<CourseDetail> courseDetailList;

}

