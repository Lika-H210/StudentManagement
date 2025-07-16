package raisetech.StudentManagement.service.normalizer;

import java.text.Normalizer;
import org.springframework.stereotype.Component;

/**
 * 検索条件SearchCriteriaの各項目を検索可能な形式に変換するクラスです
 */
@Component
public class SearchCriteriaNormalizer {

  /**
   * カナ名の検索文字列について平仮名と半角カタカナを角カタカナに、またスペースはすべて半角に変換します。
   */
  public String kanaNameNormalize(String kanaName) {
    if (kanaName == null || kanaName.isEmpty()) {
      return kanaName;
    }

    // 半角 → 全角（スペースは半角）の正規化
    String normalizedKanaName = Normalizer.normalize(kanaName, Normalizer.Form.NFKC);

    // ひらがな → カタカナ
    StringBuilder builder = new StringBuilder();
    for (char c : normalizedKanaName.toCharArray()) {
      if (c >= 'ぁ' && c <= 'ゖ') {
        builder.append((char) (c + 0x60));
      } else {
        builder.append(c);
      }
    }

    return builder.toString();
  }
}
