package raisetech.StudentManagement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"studentId", "name", "kanaName", "age"}) //項目の表示順を固定
public class Student {

  private String studentId;
  private String name;
  private String kanaName;
  private Integer age;


  public Student(String studentId, String name, String kanaName, Integer age) {
    this.studentId = studentId;
    this.name = name;
    this.kanaName = kanaName;
    this.age = age;
  }
  
  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getKanaName() {
    return kanaName;
  }

  public void setKanaName(String kanaName) {
    this.kanaName = kanaName;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
