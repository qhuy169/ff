package gt.electronic.ecommerce.config;

import gt.electronic.ecommerce.utils.Utils;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * @author quang huy
 * @created 09/09/2025 - 9:01 PM
 */
@Getter
@ToString
public class CodeConfig {
  public static final char PATTERN_PLACEHOLDER = '#';

  public static class Charset {
    public static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS = "0123456789";

    public static final String SPECIAL_CHARACTER = "@$!%*?&";
  }

  private final int length;
  private final String charset;
  private final String prefix;
  private final String postfix;
  private final String pattern;

  public CodeConfig(Integer length, String charset, String prefix, String postfix, String pattern) {
    if (length == null) {
      length = 8;
    }

    if (charset == null) {
      charset = Charset.ALPHANUMERIC;
      if (Objects.equals(pattern, Utils.REGEX_PASSWORD)) {
        charset = charset.concat(Charset.SPECIAL_CHARACTER);
      }
    }

    this.length = length;
    this.charset = charset;
    this.prefix = prefix;
    this.postfix = postfix;
    this.pattern = pattern;
  }

  public static CodeConfig length(int length) {
    return new CodeConfig(length, Charset.ALPHANUMERIC, null, null, null);
  }

  public static CodeConfig pattern(String pattern) {
    return new CodeConfig(Utils.DEFAULT_GENERATE_LENGTH, Charset.ALPHANUMERIC, null, null, pattern);
  }

  public CodeConfig withCharset(String charset) {
    return new CodeConfig(length, charset, prefix, postfix, pattern);
  }

  public CodeConfig withPrefix(String prefix) {
    return new CodeConfig(length, charset, prefix, postfix, pattern);
  }

  public CodeConfig withPostfix(String postfix) {
    return new CodeConfig(length, charset, prefix, postfix, pattern);
  }
}
