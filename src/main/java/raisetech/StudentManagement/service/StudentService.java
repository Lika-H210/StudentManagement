package raisetech.StudentManagement.service;

import java.util.ArrayList;
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

  //studentsリスト全件取得
  public List<Student> searchStudentsList() {
    return repository.searchStudents();
  }

  //students_coursesリスト全件取得
  public List<StudentsCourses> searchStudentsCourseList() {
    return repository.searchStudentsCourses();
  }

  //student 1件取得
  public StudentDetail searchStudent(Integer studentId) {
    Student student = new Student();
    List<StudentsCourses> studentsCourses = new ArrayList<>();
    studentsCourses.add(new StudentsCourses());
    StudentDetail studentDetail = new StudentDetail(student, studentsCourses);
    studentDetail.setStudent(repository.searchStudentsByStudentId(studentId));
    studentDetail.setStudentsCourses(repository.searchStudentsCoursesByStudentId(studentId));
    return studentDetail;
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

  //生徒情報とコース情報の更新
  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses course : studentDetail.getStudentsCourses()) {
        repository.updateStudentCourse(course);
      }
    }
  }


}

