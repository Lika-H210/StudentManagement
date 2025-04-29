package raisetech.StudentManagement.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
import raisetech.StudentManagement.domain.criteria.StudentDetailSearchCriteria;
import raisetech.StudentManagement.exception.ErrorResponse;
import raisetech.StudentManagement.exception.TestException;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;
import raisetech.StudentManagement.view.JsonViews;

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
   * 指定条件を満たす受講生詳細情報の一覧を取得します。 除外対象:キャンセル扱い（`isDeleted=true`）の受講生は検索条件に関わらず除外されます。
   *
   * @return 指定条件を満たす受講生詳細情報の一覧(条件によらずキャンセル扱いの受講生は含まれない)
   */
  @Operation(summary = "検索", description = "条件を満たす受講生詳細情報の一覧を検索します。※キャンセル扱いの受講生は除外")
  @GetMapping("/studentsList")
  public ResponseEntity<List<StudentDetail>> getStudentDetailList(
      @Parameter(name = "criteria", description = "検索条件")
      @Valid StudentDetailSearchCriteria criteria) {
    List<StudentDetail> getStudentDetailList = service.searchStudentDetailListByCriteria(criteria);
    return ResponseEntity.ok(getStudentDetailList);
  }

  /**
   * 指定された`studentId` に紐づく受講生及び受講コース情報(受講生詳細情報)を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報取得(受講生1名分)
   */
  @Operation(summary = "受講生詳細情報の検索", description = "対象受講生1名分の受講生詳細情報を検索します。")
  @GetMapping("/student/{studentId}")
  public StudentDetail getStudentDetail(
      @Parameter(name = "studentId", description = "検索対象の受講生ID", required = true, example = "1")
      @PathVariable @NotNull @Min(1) Integer studentId) {
    return service.getStudentDetail(studentId);
  }

  /**
   * 受講生の新規登録を行います。 任意:新規受講生に紐づく受講コース情報も同時に登録可能です。
   *
   * @param studentDetail 登録する受講生情報（必要に応じて受講コース情報を含む）
   * @return 登録された受講生情報(受講コース情報を含む場合あり)
   */
  @Operation(summary = "受講生詳細情報の登録", description = "受講生詳細情報の新規登録を行います。必須：受講生情報、任意：受講コース情報")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "登録成功"),
      @ApiResponse(responseCode = "400", description = "バリデーションエラー",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudentDetail(
      @Validated(OnRegisterStudent.class)
      @RequestBody @JsonView(JsonViews.OnRegister.class) StudentDetail studentDetail) {
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
  @Operation(summary = "受講生詳細情報の更新", description = "受講生詳細情報を修正します。")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "更新成功"),
      @ApiResponse(responseCode = "400", description = "入力データの不正エラー",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @JsonView(JsonViews.OnAll.class)
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudentDetail(
      @Validated(OnUpdate.class)
      @RequestBody StudentDetail studentDetail) {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("正常に更新されました");
  }

  //例外処理のテスト用メソッド
  @GetMapping("/exception")
  public List<StudentDetail> getException() throws TestException {
    throw new TestException("exceptionテスト用メソッドです。");
  }

}
