package raisetech.StudentManagement.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentsCourses;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;

/**
 * service: 受講生および受講コース情報の検索・登録・更新を管理するサービスクラス。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }


  /**
   * 受講生及び受講生に紐づくコース情報(受講生詳細情報)の一覧を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は除外されます。
   *
   * @return 受講生詳細情報の一覧(キャンセル扱いの受講生の受講生詳細情報を除く)
   */
  public List<StudentDetail> getStudentDetailList() {
    List<Student> students = repository.searchStudents();
    List<StudentsCourses> studentsCourses = repository.searchStudentsCourses();
    return converter.convertStudentDetails(students, studentsCourses);
  }

  /**
   * 指定された`studentId` に紐づく受講生及び受講コース情報(受講生詳細情報)を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報取得(受講生1名分)
   */
  public StudentDetail getStudentDetail(Integer studentId) {
    Student student = repository.searchStudentsByStudentId(studentId);
    List<StudentsCourses> studentsCourses = repository.searchStudentsCoursesByStudentId(studentId);
    return new StudentDetail(student, studentsCourses);
  }

  /**
   * 受講生の新規登録を行います。 任意:新規受講生に紐づく受講コース情報も同時に登録可能です。
   *
   * @param studentDetail 登録する受講生情報（必要に応じて受講コース情報を含む）
   * @return 登録された受講生情報(受講コース情報を含む場合あり)
   */
  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail) {
    repository.registerStudent(studentDetail.getStudent());
    Integer studentId = studentDetail.getStudent().getStudentId();
    List<StudentsCourses> registeredCourses = registerStudentCourses(studentId,
        studentDetail.getStudentsCourses());

    return new StudentDetail(studentDetail.getStudent(), registeredCourses);
  }

  /**
   * 受講コースの新規登録を行います。 補足:登録にはコース名(course)が必須です。
   *
   * @param studentId       登録対象の受講生ID
   * @param studentsCourses 登録する受講コース情報のリスト
   * @return 登録対象となる受講コースの有無により下記いづれかを返します。
   * <ul>
   *   <li>なし:空のリスト</li>
   *   <li>あり:指定した `studentId` に紐づく登録済みの受講コース一覧</li>
   * </ul>
   */
  @Transactional
  public List<StudentsCourses> registerStudentCourses(Integer studentId,
      List<StudentsCourses> studentsCourses) {
    //受講コース情報がない場合は空リストを返す
    if (studentsCourses == null || studentsCourses.isEmpty()) {
      return Collections.emptyList();
    }
    //登録処理を実行(studentCourse.courseに入力がある場合のみ)
    studentsCourses.forEach(studentCourse -> {
      if (studentCourse.getCourse() != null && !studentCourse.getCourse().trim().isEmpty()) {
        studentCourse.setStudentId(studentId);
        repository.registerStudentCourse(studentCourse);
      }
    });
    return repository.searchStudentsCoursesByStudentId(studentId);
  }

  /**
   * 受講生及び受講コース情報の更新を行います。
   */
  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    if (studentDetail.getStudentsCourses() != null) {
      for (StudentsCourses studentCourse : studentDetail.getStudentsCourses()) {
        repository.updateStudentCourse(studentCourse);
      }
    }
  }
}

