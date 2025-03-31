package gt.electronic.ecommerce.models.enums;

/**
 * @author quang huy
 * @created 08/09/2025 - 6:09 PM
 * @project gt-backend
 */
public enum ERole {
  // Admin role is the administrator's role
  ROLE_ADMIN(Names.ADMIN),
  // Seller is a person who sells goods to consumers.
  ROLE_SELLER(Names.SELLER),
  // Customers are the people who have the conditions to make purchasing
  // decisions. They are the beneficiaries of the characteristics and quality of
  // the product or service.
  ROLE_CUSTOMER(Names.CUSTOMER),
  // Manage product price, customers, shipping, orders and sales report
  ROLE_SHIPPER(Names.SHIPPER),
  // Manage questions and reviews
  ROLE_SALESPERSON(Names.SALESPERSON),
  // Manage categories, bv.brands, products, articles and menus
  ROLE_EDITOR(Names.EDITOR),
  // View products, view orders and update order status
  ROLE_ASSISTANT(Names.ASSISTANT);

  public static class Names {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SELLER = "ROLE_SELLER";
    public static final String CUSTOMER = "ROLE_CUSTOMER";
    public static final String SALESPERSON = "ROLE_SALESPERSON";
    public static final String EDITOR = "ROLE_EDITOR";
    public static final String ASSISTANT = "ROLE_ASSISTANT";
    public static final String SHIPPER = "ROLE_SHIPPER";
  }

  private final String label;

  private ERole(String label) {
    this.label = label;
  }

  public String toString() {
    return this.label;
  }
}
