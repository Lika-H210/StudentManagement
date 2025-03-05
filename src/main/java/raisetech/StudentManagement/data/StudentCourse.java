package raisetech.StudentManagement.data;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterCourse;
import raisetech.StudentManagement.validation.StudentValidation.OnRegisterStudent;
import raisetech.StudentManagement.validation.StudentValidation.OnUpdate;

/**
 * Entity Class: 受講生 (`students_courses`) テーブルのエンティティクラス。 受講コース情報を管理し、データベースと対応しています。
 */
@Getter
@Setter
public class StudentCourse {

  /**
   * コースID (主キー)
   */
  @Null(groups = {OnRegisterStudent.class,
      OnRegisterCourse.class}, message = "登録時はコースIDは不要です。")
  @NotNull(groups = OnUpdate.class, message = "更新時はコースIDが必須です。")
  private Integer courseId;

  /**
   * 受講生ID (外部キー: `students` テーブルの `studentId` と紐づく, 必須)
   */
  @Null(groups = OnRegisterStudent.class, message = "受講性情報と受講コース情報の同時登録時は受講生IDは不要です。")
  @NotNull(groups = OnUpdate.class, message = "受講コース情報の更新時は受講生IDが必須です。")
  @NotNull(groups = OnRegisterCourse.class, message = "受講コース情報の登録時は受講生IDが必須です。")
  private Integer studentId;

  /**
   * 受講コース名 (必須)
   */
  @NotEmpty(groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "コース名は必須です。")
  private String course;

  /**
   * 受講開始日 (任意)
   */
  private LocalDate startDate;

  /**
   * 受講終了日 (任意)
   */
  @FutureOrPresent(groups = {OnRegisterStudent.class, OnRegisterCourse.class, OnUpdate.class},
      message = "受講終了日は未来または現在の日付を入力してください。")
  private LocalDate endDate;
}
