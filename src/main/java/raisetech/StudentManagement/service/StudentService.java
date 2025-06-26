package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.custom.IllegalResourceAccessException;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;

/**
 * 受講生情報および受講コース情報の管理を行うサービスクラスです。 このクラスでは、受講生や受講コースの検索・登録・更新処理などのロジックを提供します。
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
   * 受講生の詳細情報一覧を取得します。(但し、キャンセル扱い(isDeleted=true)の受講生は除きます）
   *
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> searchStudentDetailList() {
    List<Student> studentList = repository.searchStudentList();
    List<StudentCourse> searchStudentsCourseList = repository.searchStudentCourseList();
    return converter.convertToStudentDetail(studentList, searchStudentsCourseList);
  }

  /**
   * 指定された publicId に対応する受講生の詳細情報を取得します。
   *
   * @param publicId 受講生の公開ID（UUID形式）
   * @return 該当する受講生の詳細情報
   */
  public StudentDetail searchStudentDetailByPublicId(String publicId) {

    Student student = repository.searchStudentByPublicId(publicId);
    if (student == null) {
      throw new IllegalResourceAccessException(
          "受講生情報の取得中に問題が発生しました。システム管理者までご連絡ください。");
    }

    List<StudentCourse> studentCourseList = repository.searchStudentCourseListByStudentId(
        student.getStudentId());

    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細を新規登録します。 受講生には UUID を自動生成して付与します。
   *
   * @param studentDetail 登録する受講生詳細情報
   * @return 登録された受講生詳細情報
   */
  @Transactional
  public StudentDetail registerStudentDetail(StudentDetail studentDetail)
      throws NotUniqueException {
    //登録前チェック
    if (repository.existsByEmail(studentDetail.getStudent().getEmail())) {
      throw new NotUniqueException("このメールアドレスは既に登録されています");
    }

    //受講生の登録
    studentDetail.getStudent().setPublicId(UUID.randomUUID().toString());
    repository.registerStudent(studentDetail.getStudent());

    //コース情報の登録
    Integer studentId = studentDetail.getStudent().getStudentId();
    registerStudentCourse(studentId, studentDetail.getStudentCourseList());

    return studentDetail;
  }

  /**
   * 指定された受講生IDに紐づく受講コースを登録します。初期値は必要に応じ設定されます。
   *
   * @param studentId         対象の受講生の受講生のID
   * @param studentCourseList 登録するコース情報
   */
  @Transactional
  public void registerStudentCourse(Integer studentId, List<StudentCourse> studentCourseList) {
    studentCourseList.forEach(studentCourse -> {
      initializeStudentCourse(studentId, studentCourse);
      repository.registerStudentCourse(studentCourse);
    });
  }

  /**
   * 受講コースの初期値の設定を行います。受講生IDと開始日の指定がある場合は修了日を設定します。
   *
   * @param studentId     対象の受講生 ID
   * @param studentCourse 初期設定対象の受講コース
   */
  private void initializeStudentCourse(Integer studentId, StudentCourse studentCourse) {
    studentCourse.setStudentId(studentId);
    LocalDate startDate = studentCourse.getStartDate();
    if (startDate != null) {
      studentCourse.setEndDate(startDate.plusMonths(6));
    }
  }

  /**
   * 受講生詳細情報を更新します。
   *
   * @param studentDetail 更新対象の受講生詳細情報
   */
  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) throws NotUniqueException {
    //更新前チェック
    Student student = studentDetail.getStudent();
    String publicId = student.getPublicId();
    if (repository.searchStudentByPublicId(publicId) == null) {
      throw new IllegalResourceAccessException(
          "受講生情報の取得中に問題が発生しました。システム管理者までご連絡ください。");
    } else if (repository.existsByEmailExcludingPublicId(publicId, student.getEmail())) {
      throw new NotUniqueException("このメールアドレスは使用できません。");
    }

    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> {
          repository.updateStudentCourse(studentCourse);
        });
  }

}
