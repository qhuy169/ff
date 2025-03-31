package gt.electronic.ecommerce.utils;

import gt.electronic.ecommerce.config.CodeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author quang huy
 * @created 09/09/2025 - 9:17 PM
 */
public final class GenerateUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(GenerateUtil.class.getName());
  private static final Random RND = new Random(System.currentTimeMillis());

  public static String generate(CodeConfig config) {
    StringBuilder sb = new StringBuilder();
    char[] chars = config.getCharset().toCharArray();

    char[] pattern = new char[config.getLength()];

    do {
      sb = new StringBuilder();
      for (int i = 0; i < pattern.length; i++) {
        sb.append(chars[RND.nextInt(chars.length)]);
      }
    } while (config.getPattern() != null && !sb.toString().matches(config.getPattern()));

    if (config.getPostfix() != null) {
      sb.append(config.getPostfix());
    }

    if (config.getPrefix() != null) {
      sb.insert(0, config.getPrefix());
    }

    return sb.toString();
  }
}
