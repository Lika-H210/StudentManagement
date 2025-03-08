package raisetech.StudentManagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;

/**
 * Entity Class: 受講生 (`students`) テーブルのエンティティクラス。 受講生情報を管理し、データベースと対応しています。
 */
@Getter
@Setter
public class Student {

  /**
   * 受講生ID (主キー)
   */
  @Null(groups = OnRegisterStudent.class, message = "新規登録時は受講生IDは不要です。")
  @NotNull(groups = OnUpdate.class, message = "更新時は受講生IDが必須です。")
  @Min(value = 1,
      groups = OnUpdate.class,
      message = "受講生IDは1以上の数字である必要があります。")
  private Integer studentId;

  /**
   * 受講生氏名 (必須)
   */
  @NotEmpty(groups = {OnRegisterStudent.class, OnUpdate.class}, message = "氏名は必須です。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "氏名は100文字以内である必要があります。")
  private String fullName;

  /**
   * 受講生氏名のフリガナ (必須)
   */
  @NotEmpty(groups = {OnRegisterStudent.class, OnUpdate.class}, message = "フリガナは必須です。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "フリガナは100文字以内である必要があります。")
  private String fullNameKana;

  /**
   * ニックネーム
   */
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "ニックネームは100文字以内である必要があります。")
  private String nickname;

  /**
   * メールアドレス
   */
  @Email(groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "正しいメールアドレスを入力してください。")
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "メールアドレスは100文字以内である必要があります。")
  private String mailAddress;

  /**
   * 居住エリア
   */
  @Size(max = 100,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "居住エリアは100文字以内である必要があります。")
  private String residenceArea;

  /**
   * 年齢
   */
  @Min(value = 0,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "年齢は0以上である必要があります。")
  private Integer age;

  /**
   * 性別
   */
  @Pattern(regexp = "Male|Female|Other|^$",
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "性別は 'Male', 'Female', 'Other' のいずれかを入力してください。")
  private String sex;

  /**
   * 備考
   */
  @Size(max = 1000,
      groups = {OnRegisterStudent.class, OnUpdate.class},
      message = "備考は1000文字以内である必要があります。")
  private String remark;

  /**
   * キャンセルフラグ (`true` の場合はキャンセル扱い)
   */
  private boolean isDeleted;

}