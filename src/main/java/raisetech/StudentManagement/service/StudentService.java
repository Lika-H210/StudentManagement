package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.date.Student;
import raisetech.StudentManagement.date.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    List<Student> studentList = repository.searchStudentList();
    return studentList.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() < 40)
        .toList();
  }

  //todo:引数をpublicIdに変更する
  public Student searchStudentById(Integer studentId) {
    return repository.searchStudentById(studentId);
  }

  public List<StudentCourse> searchStudentCourseList() {
    List<StudentCourse> courseList = repository.searchStudentCourseList();
    return courseList.stream()
        .filter(course -> course.getCourse().equals("Java"))
        .toList();
  }


}
