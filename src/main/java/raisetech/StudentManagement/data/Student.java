package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;
import raisetech.StudentManagement.view.JsonViews;


/**
 * Entity Class: 受講生 (`students`) テーブルのエンティティクラス。 受講生情報を管理し、データベースと対応しています。
 */
@Schema(description = "受講生情報")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Student {

  @Schema(description = "受講生ID (主キー)", example = "1")
  @JsonView(JsonViews.OnAll.class)
  @Null(groups = OnRegisterStudent.class, message = "新規登録時は受講生IDは不要です。")
  @NotNull(groups = OnUpdate.class, message = "更新時は受講生IDが必須です。")
  @Min(value = 1,
      groups = OnUpdate.class,
      message = "受講生IDは1以上の数字である必要があります。")
  private Integer studentId;

  @Schema(description = "受講生氏名", example = "山田_太郎")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @NotEmpty(groups = {OnRegisterStudent.class, OnUpdate.class}, message = "氏名は必須です。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "氏名は100文字以内である必要があります。")
  private String fullName;

  @Schema(description = "受講生氏名のフリガナ", example = "ヤマダ_タロウ")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @NotEmpty(groups = {OnRegisterStudent.class, OnUpdate.class}, message = "フリガナは必須です。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "フリガナは100文字以内である必要があります。")
  private String fullNameKana;

  @Schema(description = "ニックネーム", example = "たろちゃん")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "ニックネームは100文字以内である必要があります。")
  private String nickname;

  @Schema(description = "メールアドレス", example = "taro.yamada@example.com")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Email(groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "正しいメールアドレスを入力してください。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "メールアドレスは100文字以内である必要があります。")
  private String mailAddress;

  @Schema(description = "居住エリア", example = "東京都_練馬区")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "居住エリアは100文字以内である必要があります。")
  private String residenceArea;

  @Schema(description = "年齢", example = "25")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Min(value = 0,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "年齢は0以上である必要があります。")
  @Max(value = 150,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "年齢は150以下である必要があります。")
  private Integer age;

  @Schema(description = "性別 ('Male', 'Female', 'Other' のいずれか)", example = "Male")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Pattern(regexp = "Male|Female|Other|^$",
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "性別は 'Male', 'Female', 'Other' のいずれかを入力してください。")
  private String sex;

  @Schema(description = "備考", example = "特になし")
  @JsonView({JsonViews.OnRegister.class, JsonViews.OnAll.class})
  @Size(max = 1000,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "備考は1000文字以内である必要があります。")
  private String remark;

  @Schema(description = "キャンセルフラグ (`true` の場合はキャンセル扱い)", example = "false")
  @JsonView({JsonViews.OnAll.class})
  @JsonProperty("isDeleted")
  private boolean isDeleted;

}