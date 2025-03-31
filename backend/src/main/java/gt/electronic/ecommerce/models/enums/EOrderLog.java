package gt.electronic.ecommerce.models.enums;

public enum EOrderLog {
    LOG_PICKED(Names.LOG_PICKED);

    public static class Names {
        public static final String LOG_PICKED = "Đơn hàng đã được giao cho người vận chuyển";
    }

    private final String label;

    EOrderLog(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }
}
