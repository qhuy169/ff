package gt.electronic.ecommerce.models.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EDiscountType {
    DISCOUNT_SHOP_PRICE,
    DISCOUNT_SHOP_PERCENT,
    DISCOUNT_BILL_PRICE,
    DISCOUNT_BILL_PERCENT;

    @JsonCreator
    public static EDiscountType fromString(String value) {
        for (EDiscountType type : EDiscountType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid discount type: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
