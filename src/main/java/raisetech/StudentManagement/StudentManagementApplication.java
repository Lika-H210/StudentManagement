package raisetech.StudentManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  private String name = "Enami Koji";
  private Integer age = 30;
  private Map<Integer, Student> student = new HashMap<>();

  //初期はMapに1件のデータが入っている。
  {
    student.put(1, new Student("Enami Koji", 30));
  }

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentInfo")
  public Map<Integer, Student> getStudentInfo() {
    return student;
  }

  //新規受講生の登録
  @PostMapping("/studentInfo")
  public void addStudentInfo(String name, Integer age) {
    //キーを作成
    int newKey = student.keySet().stream()
        .mapToInt(Integer::intValue)
        .max()
        .orElse(0) + 1;
    Student newStudent = new Student(name, age);
    student.put(newKey, newStudent);
  }

  //引数Studentで登録処理ver.
  @PostMapping("/studentInfoByStudent")
  public void addStudentInfoByStudent(Student newStudent) {
    //キーを作成
    int newKey = student.keySet().stream()
        .mapToInt(Integer::intValue)
        .max()
        .orElse(0) + 1;
    student.put(newKey, newStudent);
  }

  //keyを参照し対象の受講生情報を更新
  @PutMapping("/studentInfo")
  public void putUpdateStudentInfo(Integer key, String name, Integer age) {
    Student target = student.get(key);
    if (Objects.nonNull(target)) {
      target.setName(name);
      target.setAge(age);
    }
  }

  //keyを参照し対象の受講生情報を部分更新
  @PatchMapping("/studentInfo")
  public void patchUpdateStudentInfo(Integer key, String name, Integer age) {
    Student target = student.get(key);
    if (Objects.isNull(target)) {
      return;
    }
    if (Objects.nonNull(name)) {
      target.setName(name);
    }
    if (Objects.nonNull(age)) {
      target.setAge(age);
    }
  }
}
