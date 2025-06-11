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
import raisetech.StudentManagement.service.converter.StudentConverter;

@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  public List<StudentDetail> searchStudentDetailList() {
    List<Student> studentList = repository.searchStudentList();
    List<StudentCourse> searchStudentsCourseList = repository.searchStudentCourseList();
    return converter.convertToStudentDetail(studentList, searchStudentsCourseList);
  }

  public StudentDetail searchStudentDetailByPublicId(String publicId) {
    Student student = repository.searchStudentByPublicId(publicId);
    List<StudentCourse> studentCourseList = repository.searchStudentCourseListByStudentId(
        student.getStudentId());
    return new StudentDetail(student, studentCourseList);
  }

  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail) {
    //受講生の登録
    //Todo:emailの重複チェック（例外処理作成後に実装）
    studentDetail.getStudent().setPublicId(UUID.randomUUID().toString());
    repository.registerStudent(studentDetail.getStudent());

    //コース情報の登録
    Integer studentId = studentDetail.getStudent().getStudentId();
    if (studentDetail.getStudentCourseList() != null
        && !studentDetail.getStudentCourseList().isEmpty()) {
      registerStudentCourse(studentId, studentDetail.getStudentCourseList().getFirst());
    }
    return studentDetail;
  }

  @Transactional
  public void registerStudentCourse(Integer studentId, StudentCourse studentCourse) {
    initializeStudentCourse(studentId, studentCourse);
    if (studentCourse.getCourse() != null && !studentCourse.getCourse().isEmpty()) {
      repository.registerStudentCourse(studentCourse);
    }
  }

  private void initializeStudentCourse(Integer studentId, StudentCourse studentCourse) {
    studentCourse.setStudentId(studentId);
    LocalDate startDate = studentCourse.getStartDate();
    if (startDate != null) {
      studentCourse.setEndDate(startDate.plusMonths(6));
    }
  }

  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    if (studentDetail.getStudentCourseList() != null) {
      studentDetail.getStudentCourseList()
          .forEach(studentCourse -> {
            repository.updateStudentCourse(studentCourse);
          });
    }
  }

}
