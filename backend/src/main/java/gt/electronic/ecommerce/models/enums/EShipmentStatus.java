package gt.electronic.ecommerce.models.enums;

import org.apache.poi.ss.formula.functions.Na;

import javax.naming.Name;

public enum EShipmentStatus {
    PICKED(Names.PICKED),
    SHIPPING(Names.SHIPPING),
    COMPLETED(Names.COMPLETED),
    CANCELLED(Names.CANCELLED);

    public static class Names {
        public static final String PICKED = "Đã lấy ";
        public static final String SHIPPING = "Đang vận chuyển";
        public static final String COMPLETED = "Đã hoàn thành";
        public static final String CANCELLED = "Đã bị hủy";
    }

    private final String label;

    private EShipmentStatus(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}
