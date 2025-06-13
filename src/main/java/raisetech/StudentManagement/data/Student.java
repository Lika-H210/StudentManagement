package raisetech.StudentManagement.data;

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

/**
 * 受講生情報を表すエンティティクラスです。 DBのstudentsテーブルに対応します。
 */
@Getter
@Setter
@Validated
public class Student {

  @Null(groups = RegisterGroup.class, message = "登録時は受講生IDは不要です")
  private Integer studentId;

  @Null(groups = RegisterGroup.class, message = "登録時はIDは不要です")
  @NotBlank(groups = UpdateGroup.class, message = "更新時はIDは必須です")
  @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
      groups = UpdateGroup.class,
      message = "入力の形式に誤りがあります")
  private String publicId;

  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "名前の入力は必須です")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "名前は50文字以内で入力してください")
  private String fullName;

  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class}, message = "カナ名の入力は必須です")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "カナ名は50文字以内で入力してください")
  private String kanaName;

  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "ニックネームは50文字以内で入力してください")
  private String nickname;

  @NotBlank(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "メールアドレスの入力は必須です")
  @Email(groups = {RegisterGroup.class, UpdateGroup.class},
      message = "正しいメールアドレスの形式で入力してください")
  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "メールアドレスは50文字以内のアドレスを入力してください")
  private String email;

  @Size(max = 50, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "お住まいは50文字以内で入力してください")
  private String region;

  @Min(value = 0, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "年齢は0以上の数値を入力してください")
  @Max(value = 150, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "年齢は150以下の数値を入力してください")
  private Integer age;

  @Pattern(regexp = "男性|女性|その他|", groups = {RegisterGroup.class, UpdateGroup.class},
      message = "男性・女性・その他 のいずれかを入力してください")
  private String sex;

  @Size(max = 1000, groups = {RegisterGroup.class, UpdateGroup.class},
      message = "備考は1000文字以内で入力してください")
  private String remark;

  private boolean isDeleted;

}
