package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  @Transactional
  public void registerStudentDetail(StudentDetail studentDetail) {
    //受講生の登録
    UUID uuid = UUID.randomUUID();
    studentDetail.getStudent().setPublicId(uuid.toString());
    repository.registerStudent(studentDetail.getStudent());

    //コース情報の登録
    Integer studentId = studentDetail.getStudent().getStudentId();
    if (studentDetail.getStudentCourseList() != null
        && !studentDetail.getStudentCourseList().isEmpty()) {
      registerStudentCourse(studentId, studentDetail.getStudentCourseList().getFirst());
    }
  }

  @Transactional
  public void registerStudentCourse(Integer studentId, StudentCourse studentCourse) {
    studentCourse.setStudentId(studentId);
    LocalDate startDate = studentCourse.getStartDate();
    if (startDate != null) {
      studentCourse.setEndDate(startDate.plusMonths(6));
    }
    if (studentCourse.getCourse() != null && !studentCourse.getCourse().isEmpty()) {
      repository.registerStudentCourse(studentCourse);
    }
  }

}
