package gt.electronic.ecommerce.models.enums;

import javax.print.attribute.standard.MediaSize;

/**
 * @author quang huy
 * @created 15/10/2025 - 3:47 PM
 */
public enum EModelName {
  CART_ITEM(Names.CART_ITEM);

  public static class Names {
    public static final String CART_ITEM = "Cart Item";
  }

  private final String label;

  private EModelName(String label) {
    this.label = label;
  }

  public String toString() {
    return this.label;
  }
}
