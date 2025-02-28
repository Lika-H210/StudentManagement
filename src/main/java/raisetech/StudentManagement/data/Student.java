package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity Class: 受講生 (`students`) テーブルのエンティティクラス。 受講生情報を管理し、データベースと対応しています。
 */
@Getter
@Setter
public class Student {

  /**
   * 受講生ID (主キー)
   */
  private Integer studentId;

  /**
   * 受講生氏名 (必須)
   */
  private String fullName;

  /**
   * 受講生氏名のフリガナ (必須)
   */
  private String fullNameKana;

  /**
   * ニックネーム
   */
  private String nickname;

  /**
   * メールアドレス
   */
  private String mailAddress;

  /**
   * 居住エリア
   */
  private String residenceArea;

  /**
   * 年齢
   */
  private Integer age;

  /**
   * 性別
   */
  private String sex;

  /**
   * 備考
   */
  private String remark;

  /**
   * キャンセルフラグ (`true` の場合はキャンセル扱い)
   */
  private boolean isDeleted;

}