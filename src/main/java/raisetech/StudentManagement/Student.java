package raisetech.StudentManagement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"name", "age"}) //項目の表示順を固定
public class Student {

  private String name;

  private Integer age;

  public Student(String student, Integer age) {
    this.name = student;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }
}
