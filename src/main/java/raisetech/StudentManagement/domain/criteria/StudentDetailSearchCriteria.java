package raisetech.StudentManagement.domain.criteria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "検索条件")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetailSearchCriteria {

  @Schema(description = "受講生氏名", example = "山田_太郎")
  private String fullName;

  @Schema(description = "受講生氏名のフリガナ", example = "ヤマダ_タロウ")
  private String fullNameKana;

  @Schema(description = "居住エリア", example = "東京都_練馬区")
  private String residenceArea;

  @Schema(description = "受講コース名", example = "English")
  private String course;

  @Schema(description = "ステータス", example = "仮申込")
  @Pattern(regexp = "仮申込|本申込|受講中|受講完了|キャンセル",
      message = "仮申込,本申込,受講中,受講完了,キャンセル のいずれかを入力してください")
  private String status;

}
