package raisetech.StudentManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  //リスト全件取得
  public List<Student> searchStudentsList() {
    return repository.searchStudents();
  }

  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  // 新規生徒情報登録 (DB:studentsへの登録　補：courseの入力がある場合はstudents_coursesも登録)
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());

    //コース名が入力されている場合はコースを登録
    if (!studentDetail.getStudentsCourses().getFirst().getCourse().isEmpty()) {
      studentDetail.getStudentsCourses().forEach(sc ->
          sc.setStudentId(studentDetail.getStudent().getStudentId())
      );
      registerStudentCourse(studentDetail);
    }
  }

  //コース登録
  @Transactional
  public void registerStudentCourse(StudentDetail studentDetail) {
    for (StudentsCourses studentsCourses : studentDetail.getStudentsCourses()) {
      repository.registerStudentCourse(studentsCourses);
    }
  }
}
