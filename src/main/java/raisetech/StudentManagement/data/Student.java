package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講生情報を表すエンティティクラスです。 DBのstudentsテーブルに対応します。
 */
@Schema(description = "受講生情報")
@Getter
@Setter
@Validated
public class Student {

  @Schema(description = "受講生ID", example = "1")
  @JsonView(RequestViews.Update.class)
  @Null(groups = RegisterGroup.class, message = "登録時は受講生IDは不要です")
  private Integer studentId;

  @Schema(description = "外部開示用ID(UUID形式)", example = "3ab6f73c-3bc1-11f0-b608-6845f1a11345")
  @JsonView(RequestViews.Update.class)
  @Null(groups = RegisterGroup.class, message = "登録時はIDは不要です")
  @NotBlank(groups = UpdateGroup.class, message = "更新時はIDは必須です")
  @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
      groups = UpdateGroup.class,
      message = "入力の形式に誤りがあります")
  private String publicId;

  @Schema(description = "氏名", example = "田中 太郎")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "名前の入力は必須です")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "名前は50文字以内で入力してください")
  private String fullName;

  @Schema(description = "カナ名", example = "タナカ タロウ")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class}, message = "カナ名の入力は必須です")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "カナ名は50文字以内で入力してください")
  private String kanaName;

  @Schema(description = "ニックネーム", example = "たろちゃん")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "ニックネームは50文字以内で入力してください")
  private String nickname;

  @Schema(description = "Email", example = "taro@example.com")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "メールアドレスの入力は必須です")
  @Email(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "正しいメールアドレスの形式で入力してください")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "メールアドレスは50文字以内のアドレスを入力してください")
  private String email;

  @Schema(description = "お住まい", example = "東京都 葛飾区")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "お住まいは50文字以内で入力してください")
  private String region;

  @Schema(description = "年齢", example = "30")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Min(value = 0, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "年齢は0以上の数値を入力してください")
  @Max(value = 150, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "年齢は150以下の数値を入力してください")
  private Integer age;

  @Schema(description = "性別", example = "男性")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Pattern(regexp = "男性|女性|その他|", groups = {RegisterGroup.class, UpdateGroup.class},
      message = "男性・女性・その他 のいずれかを入力してください")
  private String sex;

  @Schema(description = "備考", example = "Javaコース受講検討中")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @Size(max = 1000, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "備考は1000文字以内で入力してください")
  private String remark;

  @Schema(name = "isDeleted", description = "論理削除", example = "false")
  @JsonView({RequestViews.Register.class, RequestViews.Update.class})
  @JsonProperty("isDeleted")
  private boolean isDeleted;

}
