package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import raisetech.StudentManagement.validation.UpdateGroup;
import raisetech.StudentManagement.view.RequestViews;

/**
 * 受講コースの申込ステータスを表すエンティティクラスです。 DBのcourse_statusテーブルに対応します。
 */
@Schema(description = "受講コースの申込ステータス")
@Getter
@Setter
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class CourseStatus {

  @Schema(description = "ステータスID（登録時に自動採番される一意のID。以降変更不可。リクエストには含めない）", example = "1")
  @JsonView
  private Integer statusId;

  @Schema(description = "コースID（登録処理時に自動設定され、以降は変更不可。更新時は必須）", example = "1")
  @JsonView(RequestViews.Update.class)
  @NotNull(groups = UpdateGroup.class, message = "更新時はコースIDは必須です")
  private Integer courseId;

  @Schema(description = "申込ステータス", example = "仮申込")
  @JsonView(RequestViews.Update.class)
  @NotNull(groups = UpdateGroup.class, message = "申込ステータスは必須です")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了|キャンセル",
      groups = UpdateGroup.class,
      message = "仮申込・本申込・受講中・受講終了・キャンセル のいずれかを入力してください")
  private String status;

  @Schema(description = "仮申込日（登録処理時に自動設定され、以降は変更不可。リクエストには含めない）", example = "2024-12-01")
  @JsonView
  private LocalDate provisionalApplicationDate;

  @Schema(description = "本申込日（過去または当日の日付を指定）", example = "2024-12-15")
  @JsonView(RequestViews.Update.class)
  @PastOrPresent(groups = UpdateGroup.class, message = "本申込日には本日またはそれ以前の日付を入力してください")
  private LocalDate applicationDate;

  @Schema(description = "キャンセル日（過去または当日の日付を指定）", example = "2025-06-01")
  @JsonView(RequestViews.Update.class)
  @PastOrPresent(groups = UpdateGroup.class, message = "キャンセル日には本日またはそれ以前の日付を入力してください")
  private LocalDate cancelDate;

}
