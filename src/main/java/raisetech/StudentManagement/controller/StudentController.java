package raisetech.StudentManagement.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.domain.criteria.SearchCriteria;
import raisetech.StudentManagement.exception.custom.NotUniqueException;
import raisetech.StudentManagement.exception.response.CustomErrorResponse;
import raisetech.StudentManagement.service.StudentService;
import raisetech.StudentManagement.validation.RegisterGroup;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講生詳細情報（StudentDetail）の管理を行う REST API のコントローラーです。
 * <p>
 * このクラスでは受講生詳細情報に関する取得・登録・更新の処理を提供します。
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
  @Operation(summary = "受講生詳細一覧を取得", description = "検索条件に一致する受講生詳細情報の一覧を取得します。（但し、キャンセル扱いの受講生は除きます。）")
  @ApiResponse(
      responseCode = "200",
      description = "リクエストが正常に処理された場合",
      content = @Content(
          mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "リクエストパラメータがバリデーションルールに適合しない場合<br>"
          + "(errorCord,errorStatus,messageまたはfieldErrorMessagesが返ります。)",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CustomErrorResponse.class)
      )
  )
  @ApiResponse(
      responseCode = "500",
      description = "サーバー内部で予期しないエラーが発生した場合<br>"
          + "（まだ未実装のため形式・内容が異なる可能性かあります。）",
      content = @Content()
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentDetailList(
      @Validated @ModelAttribute SearchCriteria criteria) {
    return service.searchStudentDetailList(criteria);
  }

  /**
   * 指定された publicId に対応する受講生の詳細情報を取得します。
   *
   * @param publicId 受講生の公開ID（UUID形式）
   * @return 該当する受講生の詳細情報
   */
  @Operation(summary = "個人の受講生詳細を取得", description = "指定されたpublicIdと紐づく受講生の詳細情報を取得します。")
  @ApiResponse(
      responseCode = "200",
      description = "リクエストが正常に処理された場合",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = StudentDetail.class)
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "リクエストパラメータがバリデーションルールに適合しない場合<br>"
          + "(errorCord,errorStatus,messageまたはfieldErrorMessagesが返ります。)",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CustomErrorResponse.class)
      )
  )
  @ApiResponse(
      responseCode = "404",
      description = "対象IDの受講生が見つからなかった場合<br>"
          + "(400同様の形式でerrorCord,errorStatus,messagesが返ります。fieldErrorMessagesは含みません。)",
      content = @Content()
  )
  @ApiResponse(
      responseCode = "500",
      description = "サーバー内部で予期しないエラーが発生した場合<br>"
          + "（400同様の形式で表示予定ですが未実装のため形式・内容が異なる可能性かあります。）",
      content = @Content()
  )
  @GetMapping("/student/{publicId}")
  public StudentDetail getStudentByPublicId(
      @PathVariable
      @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
          message = "入力の形式に誤りがあります")
      String publicId) {
    return service.searchStudentDetailByPublicId(publicId);
  }

  /**
   * 受講生情報及び受講コース詳細情報を新規登録します。
   *
   * @param studentDetail 登録する受講生の詳細情報
   * @return 登録された受講生の詳細情報
   */
  @Operation(summary = "受講生登録処理", description = "受講生情報とそれに関連する受講コース詳細情報の登録を行います。(受講コース詳細情報の登録は任意)")
  @ApiResponse(
      responseCode = "201",
      description = "正常に新規登録が完了した場合",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = StudentDetail.class)
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "リクエストパラメータがバリデーションルールに適合しない場合<br>"
          + "(errorCord,errorStatus,messageまたはfieldErrorMessagesが返ります。)",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CustomErrorResponse.class)
      )
  )
  @ApiResponse(
      responseCode = "500",
      description = "サーバー内部で予期しないエラーが発生した場合<br>"
          + "（400同様の形式で表示予定ですが未実装のため形式・内容が異なる可能性かあります。）",
      content = @Content()
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudentDetail(
      @Validated(RegisterGroup.class)
      @JsonView(RequestViews.Register.class)
      @RequestBody StudentDetail studentDetail) throws NotUniqueException {
    StudentDetail registerStudentDetail = service.registerStudentDetail(studentDetail);
    return ResponseEntity.ok(registerStudentDetail);
  }

  /**
   * 受講生及び受講コース詳細情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   * @return 更新結果のメッセージ（成功時）
   */
  @Operation(summary = "受講生更新処理", description = "受講生情報とそれに関連する受講コース詳細情報の更新を行います。")
  @ApiResponse(
      responseCode = "200",
      description = "リクエストが正常に処理された場合",
      content = @Content(
          mediaType = "text/plain",
          schema = @Schema(implementation = String.class)
      )
  )
  @ApiResponse(
      responseCode = "400",
      description = "リクエストパラメータがバリデーションルールに適合しない場合<br>"
          + "(errorCord,errorStatus,messageまたはfieldErrorMessagesが返ります。)",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = CustomErrorResponse.class)
      )
  )
  @ApiResponse(
      responseCode = "500",
      description = "サーバー内部で予期しないエラーが発生した場合<br>"
          + "（400同様の形式で表示予定ですが未実装のため形式が異なる可能性かあります。）",
      content = @Content()
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudentDetail(
      @Validated(UpdateGroup.class)
      @JsonView(RequestViews.Update.class)
      @RequestBody StudentDetail studentDetail) throws NotUniqueException {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("更新処理が完了しました");
  }

}
