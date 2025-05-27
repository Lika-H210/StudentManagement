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
    //studentIdを作成
    List<Student> studentList = repository.searchStudentList();
    int studentId = studentList.stream()
        .map(Student::getStudentId)
        .filter(id -> id.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0) + 1;
    repository.registerStudent(String.valueOf(studentId), name, kanaName, age);
  }

  //引数Studentで登録処理ver.
  @PostMapping("/studentByStudent")
  public void registerStudentByStudent(Student newStudent) {
    //studentIdを作成
    List<Student> studentList = repository.searchStudentList();
    int studentId = studentList.stream()
        .map(Student::getStudentId)
        .filter(id -> id.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0) + 1;
    newStudent.setStudentId(String.valueOf(studentId));
    repository.registerStudentByStudent(newStudent);
  }

  //対象の受講生情報を更新
  @PutMapping("/student")
  public void putUpdateStudent(String studentId, String name, String kanaName, Integer age) {
    repository.putUpdateStudent(studentId, name, kanaName, age);
  }

  //対象の受講生情報を部分更新
  @PatchMapping("/student")
  public void patchUpdateStudent(String studentId, String name, String kanaName, Integer age) {
    repository.patchUpdateStudent(studentId, name, kanaName, age);
  }

  @DeleteMapping("/student")
  public void deleteStudent(String studentId) {
    repository.deleteStudent(studentId);
  }

}
