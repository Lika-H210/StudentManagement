package raisetech.StudentManagement.service.formatter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchConditionFormatterTest {

  private SearchConditionFormatter sut;

  @BeforeEach
  void setUp() {
    sut = new SearchConditionFormatter();
  }

  @Test
  void nullを渡すとnullが返ること() {
    String actual = sut.kanaNameFormatter(null);
    assertThat(actual).isNull();
  }

  @Test
  void 半角カナは全角カナに正規化されること() {
    String result = sut.kanaNameFormatter("ｶﾀｶﾅｱﾝ");
    assertThat(result).isEqualTo("カタカナアン");
  }

  @Test
  void ひらがなは全角カタカナに変換されること() {
    String result = sut.kanaNameFormatter("ひらがなぁゖ");
    assertThat(result).isEqualTo("ヒラガナァヶ");
  }

  @Test
  void 全角カタカナと記号はそのまま返ること() {
    String result = sut.kanaNameFormatter("カタカナァヶー");
    assertThat(result).isEqualTo("カタカナァヶー");
  }

  @Test
  void 全角スペースは半角スペースに変換されること() {
    String result = sut.kanaNameFormatter("　 ");  //全角スペース1 + 半角スペース1
    assertThat(result).isEqualTo("  ");
  }

}