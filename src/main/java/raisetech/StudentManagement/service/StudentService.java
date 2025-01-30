package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

  //studentIDの発行("S+YYMMDD+その日の登録数連番"で構成　YYMMDDは西暦年/月/日)
  public String createStudentId() {
    //YYMMDDの作成
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
    String todayFormatted = today.format(formatter);
    //登録数連番の最終番号を取得し新規登録に割り当てるべき連番を設定
    List<Student> students = repository.searchStudents();
    Integer lastRegisterCount = 0;
    for (Student student : students) {
      String datePart = student.getStudentId().substring(1, 7);
      String countPart = student.getStudentId().substring(7, 10);
      Integer registerCount = Integer.parseInt(countPart);
      if (datePart.equals(todayFormatted) && lastRegisterCount < registerCount) {
        lastRegisterCount = registerCount;
      }
    }
    String lastRegisterCountString = String.format("%03d", lastRegisterCount + 1);

    return "S" + todayFormatted + lastRegisterCountString;
  }

  // 新規生徒情報登録 (DB:studentsへの登録)
  public void registerStudent(StudentDetail studentDetail) {
    String studentId = createStudentId();

    repository.registerStudent(studentId, studentDetail.getStudent().getFullName(),
        studentDetail.getStudent().getFullNameKana(), studentDetail.getStudent().getNickname(),
        studentDetail.getStudent().getMailAddress(), studentDetail.getStudent().getResidenceArea(),
        studentDetail.getStudent().getAge(), studentDetail.getStudent().getSex(),
        studentDetail.getStudent().getRemark());

    //コース情報が入力されている場合はコースを登録
    if (!studentDetail.getStudentsCourses().getFirst().getCourseId().isEmpty()) {
      studentDetail.getStudentsCourses().getFirst().setStudentId(studentId);
      registerStudentCourse(studentDetail);
    }

  }

  public void registerStudentCourse(StudentDetail studentDetail) {
    repository.registerStudentCourse(studentDetail.getStudentsCourses().getFirst().getCourseId(),
        studentDetail.getStudentsCourses().getFirst().getStudentId(),
        studentDetail.getStudentsCourses().getFirst().getCourse(),
        studentDetail.getStudentsCourses().getFirst().getStartDate(),
        studentDetail.getStudentsCourses().getFirst().getEndDate());
  }

}
