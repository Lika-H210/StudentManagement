package raisetech.StudentManagement.domain.criteria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

/**
 * 受講生詳細検の一覧取得において、条件検索を行うための検索対象項目を定義するクラスです。
 */
@Schema(description = "受講生詳細一覧検索の全検索条件")
@Getter
@Builder
@Validated
public class SearchCriteria {

  //Student項目
  @Schema(description = "氏名の検索値（部分一致で検索）", example = "田中")
  @Size(max = 50, message = "氏名の検索値は50文字以内で入力してください")
  private String fullName;

  @Schema(description = "カナ名の検索値（部分一致で検索）", example = "タナカ")
  @Pattern(regexp = "^[ぁ-んァ-ヶｦ-ﾟー\\s\u3000]*$",
      message = "カナ名の検索値は ひらがな・カタカナ、またはスペースのみを入力してください")
  @Size(max = 50, message = "カナ名の検索値は50文字以内で入力してください")
  private String kanaName;

  @Schema(description = "emailの検索値（完全一致で検索）", example = "tanaka@exampl.com")
  @Email(message = "Emailは完全一致検索です。正しいメールアドレスの形式で入力してください")
  @Size(max = 50, message = "emailの検索値は50文字以内で入力してください")
  private String email;

  @Schema(description = "お住まいの検索値（部分一致で検索）", example = "東京都")
  @Size(max = 50, message = "お住まいの検索値は50文字以内で入力してください")
  private String region;

  @Schema(description = "年齢の検索範囲の下限値", example = "18")
  @Min(value = 0, message = "年齢の検索値は0以上で入力してください")
  @Max(value = 150, message = "年齢の検索値は150以下で入力してください")
  private Integer minAge;

  @Schema(description = "年齢の検索範囲の上限値", example = "50")
  @Min(value = 0, message = "年齢の検索値は0以上で入力してください")
  @Max(value = 150, message = "年齢の検索値は150以下で入力してください")
  private Integer maxAge;

  @Schema(description = "性別の検索値（完全一致で検索）", example = "男性")
  @Pattern(regexp = "男性|女性|その他", message = "性別の検索値は 男性・女性・その他 のいずれかで入力してください")
  private String sex;

  //StudentCourse項目
  @Schema(description = "コース名の検索値（部分一致で検索）", example = "Javaコース")
  @Size(max = 30, message = "コース名の検索値は30文字以内で入力してください")
  private String course;

  //CourseStatus項目
  @Schema(description = "コース申込ステータスの検索値（完全一致で検索）", example = "仮申込")
  @Pattern(regexp = "仮申込|本申込|受講中|受講終了|キャンセル",
      message = "ステータスは 仮申込・本申込・受講中・受講終了・キャンセル のいずれかで入力してください")
  private String status;

  public boolean hasCourseOrStatus() {
    return StringUtils.hasText(course) || StringUtils.hasText(status);
  }

}
