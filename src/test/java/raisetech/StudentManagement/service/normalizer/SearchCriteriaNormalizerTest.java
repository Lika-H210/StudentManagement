package raisetech.StudentManagement.service.normalizer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchCriteriaNormalizerTest {

  private SearchCriteriaNormalizer sut;

  @BeforeEach
  void setUp() {
    sut = new SearchCriteriaNormalizer();
  }

  @Test
  void nullを渡すとnullが返る() {
    String actual = sut.kanaNameNormalize(null);
    assertThat(actual).isNull();
  }

  @Test
  void 半角カナは全角カナに正規化される() {
    String result = sut.kanaNameNormalize("ｶﾀｶﾅｱﾝ");
    assertThat(result).isEqualTo("カタカナアン");
  }

  @Test
  void ひらがなは全角カタカナに変換される() {
    String result = sut.kanaNameNormalize("ひらがなぁゖ");
    assertThat(result).isEqualTo("ヒラガナァヶ");
  }

  @Test
  void 全角カタカナと記号はそのまま返る() {
    String result = sut.kanaNameNormalize("カタカナァヶー");
    assertThat(result).isEqualTo("カタカナァヶー");
  }

  @Test
  void 全角スペースは半角スペースに変換される() {
    String result = sut.kanaNameNormalize("　 ");  //全角スペース1 + 半角スペース1
    assertThat(result).isEqualTo("  ");
  }

  @Test
  void 入力形式の混在時はすべて全角カタカナに変換される() {
    String result = sut.kanaNameNormalize("ｶﾀｶﾅひらがなカタカナ");
    assertThat(result).isEqualTo("カタカナヒラガナカタカナ");
  }

}