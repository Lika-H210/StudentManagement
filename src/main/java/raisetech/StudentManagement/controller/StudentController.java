package raisetech.StudentManagement.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;

/**
 * Controller: 受講生の検索・更新・登録処理を実行する REST API。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生及び受講生に紐づくコース情報(受講生詳細情報)の一覧を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は除外されます。
   *
   * @return 受講生詳細情報の一覧(キャンセル扱いの受講生の受講生詳細情報を除く)
   */
  @GetMapping("/studentsList")
  public List<StudentDetail> getStudentDetailList() {
    return service.getStudentDetailList();
  }

  /**
   * 指定された`studentId` に紐づく受講生及び受講コース情報(受講生詳細情報)を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報取得(受講生1名分)
   */
  @GetMapping("/student/{studentId}")
  public StudentDetail getStudentDetail(@PathVariable @NotNull @Min(1) Integer studentId) {
    return service.getStudentDetail(studentId);
  }

  /**
   * 受講生の新規登録を行います。 任意:新規受講生に紐づく受講コース情報も同時に登録可能です。
   *
   * @param studentDetail 登録する受講生情報（必要に応じて受講コース情報を含む）
   * @return 登録された受講生情報(受講コース情報を含む場合あり)
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudentDetail(
      @Validated(OnRegisterStudent.class) @RequestBody StudentDetail studentDetail) {
    StudentDetail responsStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok(responsStudentDetail);
  }

  //Todo:登録済み生徒の新規コース登録処理が必要

  /**
   * 受講生及び受講コース情報の更新を行います。 キャンセルフラグ(論理削除)の更新もここで行います。
   *
   * @param studentDetail 更新する受講生の詳細情報
   * @return 更新完了メッセージ
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudentDetail(
      @Validated(OnUpdate.class) @RequestBody StudentDetail studentDetail) {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("正常に更新されました");
  }

  //例外処理のテスト用メソッド
  @GetMapping("/exception")
  public List<StudentDetail> getException() throws TestException {
    throw new TestException("exceptionテスト用メソッドです。");
  }
  
}
