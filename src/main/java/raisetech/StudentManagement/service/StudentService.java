package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.CourseDetail;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.criteria.SearchCriteria;
import raisetech.StudentManagement.exception.custom.IllegalResourceAccessException;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.converter.StudentConverter;
import raisetech.StudentManagement.service.normalizer.SearchCriteriaNormalizer;

/**
 * 受講生の詳細情報に関する検索・登録・更新などの業務ロジックを提供するサービスクラスです。 受講生情報、受講コース情報、コース申込ステータス情報が対象になります。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;
  private SearchCriteriaNormalizer normalizer;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter,
      SearchCriteriaNormalizer normalizer) {
    this.repository = repository;
    this.converter = converter;
    this.normalizer = normalizer;
  }

  /**
   * 受講生の詳細情報一覧を取得します。(但し、キャンセル扱い(isDeleted=true)の受講生は除きます）
   *
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> searchStudentDetailList(SearchCriteria criteria) {
    SearchCriteria normalizedCriteria = normalizeCriteria(criteria);

    List<Student> studentList = repository.searchStudentList(normalizedCriteria);
    if (studentList.isEmpty()) {
      return Collections.emptyList();
    }

    List<StudentCourse> studentsCourseList = repository.searchStudentCourseList(normalizedCriteria);
    List<CourseStatus> courseStatusList = repository.searchCourseStatusList(normalizedCriteria);
    List<CourseDetail> courseDetailList = converter.convertToCourseDetail(studentsCourseList,
        courseStatusList);

    //CourseDetailに関連する検索項目がある場合はStudentDetailのCourseDetailList=Emptyは許容されない
    if (criteria.hasCourseOrStatus()) {
      return converter.toStudentDetailFromStudentsWithCourse(studentList, courseDetailList);
    }

    //CourseDetailに関連する検索項目がない場合はStudentDetailのCourseDetailList=Emptyを許容する
    return converter.convertToStudentDetail(studentList, courseDetailList);
  }

  SearchCriteria normalizeCriteria(SearchCriteria original) {
    return SearchCriteria.builder()
        .fullName(original.getFullName())
        .kanaName(normalizer.kanaNameNormalize(original.getKanaName()))
        .email(original.getEmail())
        .region(original.getRegion())
        .minAge(original.getMinAge())
        .maxAge(original.getMaxAge())
        .sex(original.getSex())
        .course(original.getCourse())
        .status(original.getStatus())
        .build();
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

    if (studentCourseList.isEmpty()) {
      return new StudentDetail(student, new ArrayList<>());
    }

    List<Integer> targetCourseIdList = studentCourseList.stream()
        .map(StudentCourse::getCourseId)
        .toList();
    List<CourseStatus> courseStatusList = repository.searchCourseStatusListByCourseIdList(
        targetCourseIdList);

    List<CourseDetail> courseDetailList = converter.convertToCourseDetail(studentCourseList,
        courseStatusList);

    return new StudentDetail(student, courseDetailList);
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
    registerStudentCourse(studentId, studentDetail.getCourseDetailList());

    return studentDetail;
  }

  /**
   * 指定された受講生IDに紐づくコース詳細情報を登録します。初期値は必要に応じ設定されます。
   *
   * @param studentId        対象の受講生の受講生のID
   * @param courseDetailList 登録するコース詳細情報
   */
  @Transactional
  public void registerStudentCourse(Integer studentId, List<CourseDetail> courseDetailList) {
    courseDetailList.forEach(courseDetail -> {
      //コースの初期値設定と登録
      initializeStudentCourse(studentId, courseDetail.getStudentCourse());
      repository.registerStudentCourse(courseDetail.getStudentCourse());

      //ステータスの初期オブジェクト作成・登録・CourseDetailへの反映
      CourseStatus courseStatus = initializeCourseStatus(
          courseDetail.getStudentCourse().getCourseId());
      repository.registerCourseStatus(courseStatus);
      courseDetail.setCourseStatus(courseStatus);
    });
  }

  /**
   * 受講コースの初期値の設定を行います。受講生IDと開始日の指定がある場合は修了日を設定します。
   *
   * @param studentId     対象の受講生 ID
   * @param studentCourse 初期設定対象の受講コース
   */
  void initializeStudentCourse(Integer studentId, StudentCourse studentCourse) {
    studentCourse.setStudentId(studentId);
    LocalDate startDate = studentCourse.getStartDate();
    if (startDate != null) {
      studentCourse.setEndDate(startDate.plusMonths(6));
    }
  }

  /**
   * 指定されたコースIDに対応する初期状態のコース申込ステータスを生成します。
   *
   * @param courseId 対象のコースID
   * @return 初期状態のコース申込ステータスオブジェクト
   */
  CourseStatus initializeCourseStatus(Integer courseId) {
    CourseStatus courseStatus = new CourseStatus();
    courseStatus.setCourseId(courseId);
    courseStatus.setStatus("仮申込");
    courseStatus.setProvisionalApplicationDate(LocalDate.now());
    return courseStatus;
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
    studentDetail.getCourseDetailList()
        .forEach(courseDetail -> {
          repository.updateStudentCourse(courseDetail.getStudentCourse());
          repository.updateCourseStatus(courseDetail.getCourseStatus());
        });
  }

}
