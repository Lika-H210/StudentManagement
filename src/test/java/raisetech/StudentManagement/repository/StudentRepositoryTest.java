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

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  //受講生全件取得（論理削除は除く)
  @Test
  void DBのstudentsテーブル内でisDeletedがfalseのデータのみ取得できていること() {
    List<Student> actual = sut.searchStudentList();

    assertThat(actual.size()).isEqualTo(5);
    assertThat(actual)
        .allSatisfy(student -> assertThat(student.isDeleted()).isEqualTo(false));
  }

  //受講コース全件検索
  @Test
  void DBのstudentsCoursesテーブル内で全件のデータが取得できていること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();

    assertThat(actual.size()).isEqualTo(7);
  }

  //コース申込ステータス全件検索
  @Test
  void DBのCourseStatusテーブル内で全件のデータが取得できていること() {
    List<CourseStatus> actual = sut.searchCourseStatusList();

    assertThat(actual.size()).isEqualTo(7);
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

}
