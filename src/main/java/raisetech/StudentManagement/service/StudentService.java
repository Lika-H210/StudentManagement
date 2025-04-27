package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.criteria.StudentDetailSearchCriteria;
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
   * 受講生及び受講生に紐づくコース詳細情報(受講生詳細情報)の一覧を取得します。 除外対象:キャンセル扱い（`student.isDeleted=true`）の受講生は除外されます。
   *
   * @return 受講生詳細情報の一覧(キャンセル扱いの受講生の受講生詳細情報を除く)
   */
  public List<StudentDetail> getStudentDetailList() {
    List<Student> studentsList = repository.searchStudents();
    List<StudentCourse> studentsCoursesList = repository.searchStudentsCourses();
    List<CourseStatus> courseStatusList = repository.searchCoursesStatus();
    List<CourseDetail> courseDetailList = converter.convertCourseDetailList(studentsCoursesList,
        courseStatusList);
    return converter.convertStudentDetailList(studentsList, courseDetailList);
  }

  /**
   * 指定された`studentId` に紐づく受講生及び受講コース詳細情報(受講生詳細情報)を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報取得(受講生1名分)
   */
  public StudentDetail getStudentDetail(Integer studentId) {
    Student student = repository.searchStudentByStudentId(studentId);
    List<StudentCourse> studentCourseList = repository.searchStudentCoursesByStudentId(studentId);
    List<CourseDetail> courseDetailList = getCourseDetailList(studentCourseList);

    //studentIdに紐づくstudentが存在しない場合404エラーを返す。
    if (student == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "受講生が見つかりませんでした");
    }
    return new StudentDetail(student, courseDetailList);
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
    List<CourseDetail> registeredCourses = new ArrayList<>();
    //コース詳細情報がnullでも空でもないときのみコース登録処理を実行
    if (studentDetail.getCourseDetailList() != null && !studentDetail.getCourseDetailList()
        .isEmpty()) {
      registeredCourses = registerCourseDetailList(studentId, studentDetail.getCourseDetailList());
    }
    return new StudentDetail(studentDetail.getStudent(), registeredCourses);
  }

  /**
   * 受講コースの新規登録を行います。 補足:登録にはコース名(course)が必須です。
   *
   * @param studentId        登録対象の受講生ID
   * @param courseDetailList 登録する受講コース情報のリスト
   * @return 登録対象となる受講コースの有無により下記いづれかを返します。
   * <ul>
   *   <li>なし:空のリスト</li>
   *   <li>あり:指定した `studentId` に紐づく登録済みの受講コース一覧</li>
   * </ul>
   */
  @Transactional
  public List<CourseDetail> registerCourseDetailList(Integer studentId,
      List<CourseDetail> courseDetailList) {
    courseDetailList.forEach(courseDetail -> {
      //登録処理を実行(studentCourse.courseに入力がある場合のみ)
      if (courseDetail.getStudentCourse().getCourse() != null && !courseDetail.getStudentCourse()
          .getCourse().trim().isEmpty()) {
        courseDetail.getStudentCourse().setStudentId(studentId);
        repository.registerStudentCourse(courseDetail.getStudentCourse());

        //対になるCourseStatusを作成＆登録
        Integer courseId = courseDetail.getStudentCourse().getCourseId();
        CourseStatus courseStatus = initCourseStatus(courseId);
        repository.registerCourseStatus(courseStatus);
      }
    });
    List<StudentCourse> studentCourseList = repository.searchStudentCoursesByStudentId(studentId);

    return getCourseDetailList(studentCourseList);
  }

  //StudentCourseに紐づくCourseStatusを作成し初期値を設定
  static CourseStatus initCourseStatus(Integer courseId) {
    CourseStatus courseStatus = new CourseStatus();

    courseStatus.setStatus("仮申込");
    courseStatus.setCourseId(courseId);
    courseStatus.setProvisionalApplicationDate(LocalDate.now());

    return courseStatus;
  }

  /**
   * 受講生及び受講コース情報の更新を行います。
   *
   * @param studentDetail 更新する受講生情報
   */
  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());

    if (studentDetail.getCourseDetailList() != null && !studentDetail.getCourseDetailList()
        .isEmpty()) {
      studentDetail.getCourseDetailList()
          .forEach(courseDetail -> {
            repository.updateStudentCourse(courseDetail.getStudentCourse());
            repository.updateCourseStatus(courseDetail.getCourseStatus());
          });
    }
  }

  /**
   * 受講コース情報を受講詳細情報として取得する
   *
   * @param studentCourseList 取得したいコース詳細情報と関連する受講コース情報
   * @return コース詳細情報のリスト
   */
  List<CourseDetail> getCourseDetailList(List<StudentCourse> studentCourseList) {
    if (studentCourseList == null || Objects.requireNonNull(studentCourseList).isEmpty()) {
      return Collections.emptyList();
    }
    List<Integer> courseIdList = studentCourseList.stream()
        .map(StudentCourse::getCourseId)
        .collect(Collectors.toList());
    List<CourseStatus> courseStatusList = repository.searchCourseStatusByCourseIdList(courseIdList);
    return converter.convertCourseDetailList(studentCourseList, courseStatusList);
  }

  /**
   * 検索条件に一致する受講生詳細情報の一覧を取得します（キャンセル扱い（`student.isDeleted=true`）の受講生は除外されます。）
   *
   * @param criteria 検索条件
   * @return 条件を満たす受講生詳細情報の一覧(キャンセル扱いの受講生の受講生詳細情報を除く)
   */
  public List<StudentDetail> searchStudentDetailListByCriteria(
      StudentDetailSearchCriteria criteria) {
    //DBの各テーブルに対し、対応するcriteriaで検索し一覧取得
    List<StudentCourse> studentCoursesListByCriteria = repository.searchStudentsCoursesByCriteria(
        criteria);
    List<CourseStatus> courseStatusesListByCriteria = repository.searchCourseStatusesByCriteria(
        criteria);
    List<Student> studentListByCriteria = repository.searchStudentsByCriteria(criteria);

    if (studentListByCriteria.isEmpty() || studentCoursesListByCriteria.isEmpty()
        || courseStatusesListByCriteria.isEmpty()) {
      return Collections.emptyList();
    }

    List<CourseDetail> courseDetailList = converter.combineStudentCourseWithCourseStatusByCourseId(
        studentCoursesListByCriteria, courseStatusesListByCriteria);

    if (courseDetailList.isEmpty()) {
      return Collections.emptyList();
    }

    return converter.combineStudentsWithCourseDetailsByStudentId(
        studentListByCriteria, courseDetailList);
  }

}

