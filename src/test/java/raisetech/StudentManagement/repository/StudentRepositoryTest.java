package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.CourseStatus;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.criteria.SearchCriteria;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  //受講生条件検索：検索条件なしで受講生全件取得（論理削除は除く)
  @Test
  void 検索条件なしでDBのstudentsテーブル内でisDeletedがfalseのデータのみ取得できていること() {
    SearchCriteria criteria = SearchCriteria.builder().build();
    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(5);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.isDeleted()).isEqualTo(false));
  }

  //受講生条件検索：単独検索(Student.fullName：部分一致)
  @Test
  void fullNameの検索で適切な検索結果が返ってくること() {
    String fullName = "太郎";
    SearchCriteria criteria = SearchCriteria.builder()
        .fullName(fullName)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getFullName()).contains(fullName));
  }

  //受講生条件検索：単独検索(Student.kanaName：部分一致)
  @Test
  void kanaName検索で適切な検索結果が返ってくること() {
    String kanaName = "タロ";
    SearchCriteria criteria = SearchCriteria.builder()
        .kanaName(kanaName)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getKanaName()).contains(kanaName));
  }

  //受講生条件検索：単独検索(Student.email：完全一致)
  @Test
  void email検索で適切な検索結果が返ってくること() {
    String email = "suzuki@example.com";
    SearchCriteria criteria = SearchCriteria.builder()
        .email(email)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst().getEmail()).isEqualTo(email);
  }

  //受講生条件検索：単独検索(Student.region：部分一致)
  @Test
  void region検索で適切な検索結果が返ってくること() {
    String region = "都";
    SearchCriteria criteria = SearchCriteria.builder()
        .region(region)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getRegion()).contains(region));
  }

  //受講生条件検索：単独検索(Student.age：対象数値以上)
  @Test
  void minAge検索で適切な検索結果が返ってくること() {
    Integer minAge = 30;
    SearchCriteria criteria = SearchCriteria.builder()
        .minAge(minAge)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getAge()).isGreaterThanOrEqualTo(minAge));
  }

  //受講生条件検索：単独検索(Student.age：対象数値以下)
  @Test
  void maxAge検索で適切な検索結果が返ってくること() {
    Integer maxAge = 30;
    SearchCriteria criteria = SearchCriteria.builder()
        .maxAge(maxAge)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getAge()).isLessThanOrEqualTo(maxAge));
  }

  //受講生条件検索：単独検索(Student.sex：完全一致)
  @Test
  void sex検索で適切な検索結果が返ってくること() {
    String sex = "女性";
    SearchCriteria criteria = SearchCriteria.builder()
        .sex(sex)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst().getSex()).isEqualTo(sex);
  }

  //受講生条件検索：複数条件検索(Student.age：規定範囲内)
  @Test
  void minAgeとmaxAgeの間の年齢のみ検索結果として返ってくること() {
    Integer minAge = 29;
    Integer maxAge = 31;
    SearchCriteria criteria = SearchCriteria.builder()
        .minAge(minAge)
        .maxAge(maxAge)
        .build();

    List<Student> actual = sut.searchStudentList(criteria);

    assertThat(actual.size()).isEqualTo(1);
    assertThat(actual.getFirst().getAge()).isLessThanOrEqualTo(maxAge);
    assertThat(actual.getFirst().getAge()).isGreaterThanOrEqualTo(minAge);
  }

  //受講コース条件検索:検索条件なしで全受講コース取得
  @Test
  void DBのstudentsCoursesテーブル内で全件のデータが取得できていること() {
    SearchCriteria criteria = SearchCriteria.builder().build();
    List<StudentCourse> actual = sut.searchStudentCourseList(criteria);

    assertThat(actual.size()).isEqualTo(8);
  }

  //受講コース条件検索：単独検索(StudentCourse.course：部分一致)
  @Test
  void course検索で適切な検索結果が返ってくること() {
    String course = "Python";
    SearchCriteria criteria = SearchCriteria.builder()
        .course(course)
        .build();

    List<StudentCourse> actual = sut.searchStudentCourseList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getCourse()).contains(course));
  }

  //コース申込ステータス条件検索:検索条件なしで全コース申込ステータス取得
  @Test
  void DBのCourseStatusテーブル内で全件のデータが取得できていること() {
    SearchCriteria criteria = SearchCriteria.builder().build();
    List<CourseStatus> actual = sut.searchCourseStatusList(criteria);

    assertThat(actual.size()).isEqualTo(7);
  }

  //コース申込ステータス条件検索：単独検索(CourseStatus.status：完全一致)
  @Test
  void status検索で適切な検索結果が返ってくること() {
    String status = "キャンセル";
    SearchCriteria criteria = SearchCriteria.builder()
        .status(status)
        .build();

    List<CourseStatus> actual = sut.searchCourseStatusList(criteria);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.getStatus()).isEqualTo(status));
  }

  //publicIDに基づく受講生個人検索:publicId登録あり
  @Test
  void 受講生の個人検索でpublicIdが一致する受講生情報が取得できていること() {
    String publicId = "3ab6f73c-3bc1-11f0-b608-6845f1a11345";

    Student actual = sut.searchStudentByPublicId(publicId);

    assertThat(actual.getPublicId()).isEqualTo(publicId);
  }

  //publicIDに基づく受講生個人検索:publicId登録なし
  @Test
  void 受講生の個人検索でpublicIdが一致する受講生情報が存在しない場合nullが返ること() {
    String publicId = "00000000-0000-0000-0000-000000000000";

    Student actual = sut.searchStudentByPublicId(publicId);

    assertThat(actual).isNull();
  }

  //studentIDに基づく受講コース検索:複数コースあり
  @Test
  void 検索したstudentIDのコース情報を登録個数分取得できていること() {
    Integer studentId = 1;

    List<StudentCourse> actual = sut.searchStudentCourseListByStudentId(studentId);

    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual)
        .allSatisfy(course -> assertThat(course.getStudentId()).isEqualTo(studentId));
  }

  //studentIDに基づく受講コース検索:Idに紐づく登録コースなし
  @Test
  void 検索したstudentIDのと紐づくコース情報が存在しない場合に空リストが返ること() {
    Integer studentId = 3;

    List<StudentCourse> actual = sut.searchStudentCourseListByStudentId(studentId);

    assertThat(actual).isEmpty();
  }

  //courseIDに紐づくコース申込ステータスの検索:複数の対象courseIdあり
  @Test
  void 複数のcourseID情報を含むリストを基にリスト中のcourseIDに紐づくコース申込ステータスのみのリストを返すこと() {
    List<Integer> courseIdList = List.of(4, 6);

    List<CourseStatus> actual = sut.searchCourseStatusListByCourseIdList(courseIdList);

    List<Integer> actualCourseIdList = actual.stream()
        .map(CourseStatus::getCourseId)
        .toList();
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actualCourseIdList).containsExactlyInAnyOrderElementsOf(courseIdList);
  }

  //受講生登録処理
  @Test
  void 受講生登録処理実行後に受講生の全項目が登録されかつ引数のstudentに発効されたstudentIdがsetされていること() {
    Student student = new Student(
        null,
        "5f7b71c1-9012-4a9d-89a2-9d0f9b7eabcd",
        "山田 太郎",
        "ヤマダ タロウ",
        "たっちゃん",
        "taro.yamada@example.org",
        "福岡県 北九州市",
        28,
        "その他",
        "Java受講予定です",
        false
    );

    sut.registerStudent(student);

    Student registeredStudent = sut.searchStudentByPublicId(student.getPublicId());

    assertThat(registeredStudent)
        .usingRecursiveComparison()
        .isEqualTo(student);
  }

  //受講コース登録処理
  @Test
  void コース登録処理実行後にコースの全項目が登録されかつ引数のstudentCourseに発効されたcourseIdがsetされていること() {
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = new StudentCourse(
        null,
        1,
        "AIコース",
        now,
        now.plusMonths(6)
    );

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> studentCourseList = sut.searchStudentCourseListByStudentId(
        studentCourse.getStudentId());

    assertThat(studentCourseList)
        .usingRecursiveFieldByFieldElementComparator()
        .contains(studentCourse);
  }

  //コース申込ステータス登録処理
  @Test
  void コース申込ステータス登録処理実行後に() {
    CourseStatus courseStatus = new CourseStatus(
        null,
        8,
        "仮申込",
        LocalDate.now(),
        null,
        null
    );

    sut.registerCourseStatus(courseStatus);

    List<CourseStatus> courseStatusList = sut.searchCourseStatusListByCourseIdList(
        List.of(courseStatus.getCourseId()));

    assertThat(courseStatusList.getFirst())
        .usingRecursiveComparison()
        .isEqualTo(courseStatus);
  }

  //受講生更新処理：更新対応項目の更新確認
  @Test
  void 受講生更新処理実行後に受講生のID項目以外の全項目が更新されること() {
    Student student = createStudentRegisteredAsId2();
    //studentのID項目以外すべて変更
    student.setFullName("テスト 花子");
    student.setKanaName("テスト ハナコ");
    student.setNickname("テスト");
    student.setEmail("XXX@XXX.XXX");
    student.setRegion("XX県 XXX市");
    student.setAge(111);
    student.setSex("その他");
    student.setRemark("XXXコース受講");
    student.setDeleted(true);

    sut.updateStudent(student);

    Student updatedStudent = sut.searchStudentByPublicId(student.getPublicId());

    assertThat(updatedStudent)
        .usingRecursiveComparison()
        .isEqualTo(student);
  }

  //受講生更新処理：更新非対応項目の未更新確認
  @Test
  void 受講生情報のstudentIdを更新した場合にstudentIdに変更が反映されないこと() {
    Student student = createStudentRegisteredAsId2();
    //検証用に元のIDを保持
    Integer originalStudentId = student.getStudentId();
    student.setStudentId(999);

    sut.updateStudent(student);

    Student updatedStudent = sut.searchStudentByPublicId(student.getPublicId());

    assertThat(updatedStudent.getStudentId()).isEqualTo(originalStudentId);
  }

  //受講コース更新処理：更新対応項目の更新確認
  @Test
  void 受講コース更新処理実行後にコースのID項目以外の全項目が更新されること() {
    LocalDate now = LocalDate.now();
    StudentCourse studentCourse = createStudentCourseForStudentId2();
    Integer studentId = studentCourse.getStudentId();
    //studentCourseのID項目以外すべて変更
    studentCourse.setCourse("XXXコース");
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusMonths(6));

    sut.updateStudentCourse(studentCourse);

    StudentCourse updatedStudentCourse = sut.searchStudentCourseListByStudentId(studentId)
        .getFirst();

    assertThat(updatedStudentCourse)
        .usingRecursiveComparison()
        .isEqualTo(studentCourse);
  }

  //受講コース更新処理：更新非対応項目の未更新確認
  @Test
  void 受講コース情報のstudentIdを更新した場合にstudentIdに変更が反映されないこと() {
    StudentCourse studentCourse = createStudentCourseForStudentId2();
    Integer studentId = studentCourse.getStudentId();
    Integer courseId = studentCourse.getCourseId();
    studentCourse.setStudentId(1);

    sut.updateStudentCourse(studentCourse);

    StudentCourse updatedStudentCourse = sut.searchStudentCourseListByStudentId(studentId)
        .stream()
        .filter(course -> course.getCourseId().equals(courseId))
        .findFirst()
        .orElseThrow();

    assertThat(updatedStudentCourse.getStudentId()).isEqualTo(studentId);
  }

  //コース申込ステータス更新処理：更新対応項目の更新確認
  @Test
  void コース申込ステータ更新処理実行後に更新対象にID項目と仮申込日以外の全項目の変更が反映されていること() {
    CourseStatus courseStatus = createCourseStatusForCourseId3();
    Integer courseId = courseStatus.getCourseId();
    //CourseStatusのID項目と仮申込日以外変更
    courseStatus.setStatus("キャンセル");
    courseStatus.setApplicationDate(LocalDate.of(2000, 1, 1));
    courseStatus.setCancelDate(LocalDate.of(2000, 2, 1));

    sut.updateCourseStatus(courseStatus);

    CourseStatus updatedCourseStatus = sut.searchCourseStatusListByCourseIdList(List.of(courseId))
        .getFirst();

    assertThat(updatedCourseStatus)
        .usingRecursiveComparison()
        .isEqualTo(courseStatus);
  }

  //コース申込ステータス更新処理：更新非対応項目の未更新確認
  @Test
  void コース申込ステータス更新処理実行後に更新対象にstatusIdと仮申込日に変更が反映されていないこと() {
    CourseStatus courseStatus = createCourseStatusForCourseId3();
    Integer courseId = courseStatus.getCourseId();
    Integer statusId = courseStatus.getStatusId();
    LocalDate provisionalApplicationDate = courseStatus.getProvisionalApplicationDate();
    //CourseStatusのstatusIdと仮申込日を変更
    courseStatus.setStatusId(999);
    courseStatus.setProvisionalApplicationDate(LocalDate.of(2000, 1, 1));

    sut.updateCourseStatus(courseStatus);

    CourseStatus updatedCourseStatus = sut.searchCourseStatusListByCourseIdList(List.of(courseId))
        .getFirst();

    assertThat(updatedCourseStatus.getStatusId()).isEqualTo(statusId);
    assertThat(updatedCourseStatus.getProvisionalApplicationDate())
        .isEqualTo(provisionalApplicationDate);
  }

  //メースアドレスの重複チェック(チェック対象：全レコード）※登録処理を想定
  @Test
  void 登録済みemailで実行した場合にtrueが返ること() {
    boolean actual = sut.existsByEmail("taro@example.com");

    assertThat(actual).isTrue();
  }

  @Test
  void 登録済みemailで照合した場合にfalseが返ること() {
    boolean actual = sut.existsByEmail("Test@example");

    assertThat(actual).isFalse();
  }

  //メールアドレスの重複チェック(チェック対象：更新対象レコード以外）※更新処理を想定
  @Test
  void 更新対象受講生以外の受講生情報に登録されているemailを照合した結果trueが返ること() {
    String publicId = "3ab6f73c-3bc1-11f0-b608-6845f1a11345";

    boolean actual = sut.existsByEmailExcludingPublicId(publicId, "hanako@example.com");

    assertThat(actual).isTrue();
  }

  @Test
  void 更新対象受講生のemailと同じemailで照合した結果falseが返ること() {
    String publicId = "3ab6f73c-3bc1-11f0-b608-6845f1a11345";

    boolean actual = sut.existsByEmailExcludingPublicId(publicId, "taro@example.com");

    assertThat(actual).isFalse();
  }

  @Test
  void 未登録のemailで照合した結果falseが返ること() {
    String publicId = "3ab6f73c-3bc1-11f0-b608-6845f1a11345";

    boolean actual = sut.existsByEmailExcludingPublicId(publicId, "XXX@XXX.XXX");

    assertThat(actual).isFalse();
  }

  private Student createStudentRegisteredAsId2() {
    return new Student(
        2,
        "d8a0a2f1-7e5e-4f0f-b123-a1b3d1f82dd2",
        "佐藤 花子",
        "サトウ ハナコ",
        "",
        "hanako@example.com",
        "",
        null,
        "",
        "",
        false
    );
  }

  public StudentCourse createStudentCourseForStudentId2() {
    return new StudentCourse(
        3,
        2,
        "Pythonコース",
        LocalDate.of(2025, 2, 1),
        LocalDate.of(2025, 8, 1)
    );
  }

  private CourseStatus createCourseStatusForCourseId3() {
    return new CourseStatus(
        3,
        3,
        "本申込",
        LocalDate.of(2025, 7, 10),
        LocalDate.of(2025, 7, 20),
        null
    );
  }

}
