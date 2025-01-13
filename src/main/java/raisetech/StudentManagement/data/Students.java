package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Students {
  //必須：studentId, fullName, fullNameKana
  //studentId:PRIMARY KEY
  private String studentId;
  private String fullName;
  private String fullNameKana;
  private String nickname;
  private String mailAddress;
  private String residenceArea;
  private Integer age;
  private String sex;
}