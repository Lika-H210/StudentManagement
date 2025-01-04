package raisetech.StudentManagement;

public class Student {
  private String name;
  private int age;
  private String course;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getCourse() {
    return course;
  }

  public void setCourse(String course) {
    this.course = course;
  }

  @Override
  public String toString() {
    return "{" + getName() + ", " + getAge() +  "歳, " + getCourse() + "}";
  }
}
