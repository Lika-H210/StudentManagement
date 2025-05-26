package raisetech.StudentManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  public StudentRepository repository;

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }

  @GetMapping("/studentList")
  public List<Student> getStudentList() {
    return repository.searchStudentList();
  }

  @GetMapping("/student")
  public Student getStudentById(@RequestParam String studentId) {
    return repository.searchStudentById(studentId);
  }

  //新規受講生の登録
  @PostMapping("/student")
  public void registerStudent(String name, String kanaName, Integer age) {
    //キーを作成
    List<Student> studentList = repository.searchStudentList();
    int newKey = studentList.stream()
        .map(Student::getStudentId)
        .filter(id -> id.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0) + 1;
    Student newStudent = new Student(String.valueOf(newKey), name, kanaName, age);
    repository.registerStudent(newStudent);
  }

  //引数Studentで登録処理ver.
  @PostMapping("/studentByStudent")
  public void registerStudentByStudent(Student newStudent) {
    //キーを作成
    List<Student> studentList = repository.searchStudentList();
    int newKey = studentList.stream()
        .map(Student::getStudentId)
        .filter(id -> id.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0) + 1;
    newStudent.setStudentId(String.valueOf(newKey));
    repository.registerStudent(newStudent);
  }

  //keyを参照し対象の受講生情報を更新
  @PutMapping("/student")
  public void putUpdateStudent(String studentId, String name, String kanaName, Integer age) {
    repository.putUpdateStudent(studentId, name, kanaName, age);
  }

  //keyを参照し対象の受講生情報を部分更新
  @PatchMapping("/student")
  public void patchUpdateStudent(String studentId, String name, String kanaName, Integer age) {
    repository.patchUpdateStudent(studentId, name, kanaName, age);

  }

  @DeleteMapping("/student")
  public void deleteStudent(String studentId) {
    repository.deleteStudent(studentId);
  }
}
