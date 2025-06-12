package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 受講生情報を表すエンティティクラスです。 DBのstudentsテーブルに対応します。
 */
@Getter
@Setter
public class Student {

  //studentId必須 PRIKEY
  private Integer studentId;
  //publicId必須 UNIKEY　(UUID)
  private String publicId;
  //fullName必須
  private String fullName;
  //kanaName必須
  private String kanaName;
  private String nickname;
  //email必須 UNIKEY
  private String email;
  private String region;
  private Integer age;
  private String sex;
  private String remark;
  private boolean isDeleted;

}
