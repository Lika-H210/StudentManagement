package raisetech.StudentManagement.controller;

import jakarta.validation.constraints.Pattern;
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
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.exception.custom.TestException;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;


/**
 * 受講生情報の管理を行う REST API のコントローラーです。
 * <p>
 * このクラスでは受講生情報（StudentDetail）に関する取得・登録・更新の処理を提供します。
 */
@RestController
@Validated
public class StudentController {

  private StudentService service;

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }


  /**
   * 受講生の詳細情報一覧を取得します。(但し、キャンセル扱い(isDeleted=true)の受講生は除きます）
   *
   * @return 受講生詳細情報のリスト
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentDetailList();
  }

  /**
   * 指定された publicId に対応する受講生の詳細情報を取得します。
   *
   * @param publicId 受講生の公開ID（UUID形式）
   * @return 該当する受講生の詳細情報
   */
  @GetMapping("/student/{publicId}")
  public StudentDetail getStudentByPublicId(
      @PathVariable
      @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
          message = "入力の形式に誤りがあります")
      String publicId) {
    return service.searchStudentDetailByPublicId(publicId);
  }

  /**
   * 受講生情報及び受講コース情報を新規登録します。
   *
   * @param studentDetail 登録する受講生の詳細情報
   * @return 登録された受講生の詳細情報
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Validated(RegisterGroup.class)
      @RequestBody StudentDetail studentDetail) throws NotUniqueException {
    StudentDetail registerStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok(registerStudentDetail);
  }

  /**
   * 受講生及び受講コース情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   * @return 更新結果のメッセージ（成功時）
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(
      @Validated(UpdateGroup.class)
      @RequestBody StudentDetail studentDetail) {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("更新処理が完了しました");
  }

  @GetMapping("/exception")
  public StudentDetail getDetail() throws TestException {
    throw new TestException("エラーが発生しました");
  }
}
