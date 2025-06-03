package raisetech.StudentManagement.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudentList();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourseList();
  }

  //todo:引数をpublicIdに変更する
  public Student searchStudentById(Integer studentId) {
    return repository.searchStudentById(studentId);
  }

  public void registerStudent(StudentDetail studentDetail) {
    UUID uuid = UUID.randomUUID();
    studentDetail.getStudent().setPublicId(uuid.toString());
    repository.registerStudent(studentDetail.getStudent());
  }

}
