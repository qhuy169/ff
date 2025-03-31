package gt.electronic.ecommerce.models.enums;

/**
 * @author quang huy
 * @created 17/09/2025 - 10:11 PM
 * @project gt-backend
 */
public enum EOrderStatus {
    // ORDER_PENDING(Names.ORDER_PENDING),
    ORDER_AWAITING_PAYMENT(Names.ORDER_AWAITING_PAYMENT),
    ORDER_SHIPPING(Names.ORDER_SHIPPING),
    ORDER_DELIVERING(Names.ORDER_DELIVERING),
    ORDER_COMPLETED(Names.ORDER_COMPLETED),
    ORDER_CANCELLED(Names.ORDER_CANCELLED);

    // ORDER_AWAITING_FULFILLMENT(Names.ORDER_AWAITING_FULFILLMENT),
    // ORDER_AWAITING_SHIPMENT(Names.ORDER_AWAITING_SHIPMENT),
    // ORDER_AWAITING_PICKUP(Names.ORDER_AWAITING_PICKUP),
    // ORDER_PARTIALLY_SHIPPED(Names.ORDER_PARTIALLY_SHIPPED),
    // ORDER_SHIPPED(Names.ORDER_SHIPPED),
    // ORDER_DECLINED(Names.ORDER_DECLINED),
    // ORDER_REFUNDED(Names.ORDER_REFUNDED),
    // ORDER_DISPUTED(Names.ORDER_DISPUTED),
    // ORDER_MANUAL_VERIFICATION_REQUIRED(Names.ORDER_MANUAL_VERIFICATION_REQUIRED),
    // ORDER_PARTIALLY_REFUNDED(Names.ORDER_PARTIALLY_REFUNDED),
    //
    // ORDER_ready_to_pick(Names.ORDER_ready_to_pick),
    // ORDER_picking(Names.ORDER_picking),
    // ORDER_cancel(Names.ORDER_cancel),
    // ORDER_money_collect_picking(Names.ORDER_money_collect_picking),
    // ORDER_picked(Names.ORDER_picked),
    // ORDER_storing(Names.ORDER_storing),
    // ORDER_transporting(Names.ORDER_transporting),
    // ORDER_sorting(Names.ORDER_sorting),
    // ORDER_delivering(Names.ORDER_delivering),
    // ORDER_money_collect_delivering(Names.ORDER_money_collect_delivering),
    // ORDER_delivered(Names.ORDER_delivered),
    // ORDER_delivery_fail(Names.ORDER_delivery_fail),
    // ORDER_waiting_to_return(Names.ORDER_waiting_to_return),
    // ORDER_return(Names.ORDER_return),
    // ORDER_return_transporting(Names.ORDER_return_transporting),
    // ORDER_return_sorting(Names.ORDER_return_sorting),
    // ORDER_returning(Names.ORDER_returning),
    // ORDER_return_fail(Names.ORDER_return_fail),
    // ORDER_returned(Names.ORDER_returned),
    // ORDER_exception(Names.ORDER_exception),
    // ORDER_damage(Names.ORDER_damage),
    // ORDER_lost(Names.ORDER_lost);

    public static class Names {
        public static final String ORDER_PENDING = "Đang chờ xác nhận";
        public static final String ORDER_AWAITING_PAYMENT = "Đang chờ thanh toán";
        public static final String ORDER_SHIPPING = "Đang vận chuyển";
        public static final String ORDER_DELIVERING = "Đang giao hàng";
        public static final String ORDER_COMPLETED = "Đã hoàn thành";
        public static final String ORDER_CANCELLED = "Đã hủy";
        public static final String ORDER_AWAITING_FULFILLMENT = "";
        public static final String ORDER_AWAITING_SHIPMENT = "";
        public static final String ORDER_AWAITING_PICKUP = "";
        public static final String ORDER_PARTIALLY_SHIPPED = "";
        public static final String ORDER_SHIPPED = "";
        public static final String ORDER_DECLINED = "";
        public static final String ORDER_REFUNDED = "";
        public static final String ORDER_DISPUTED = "";
        public static final String ORDER_MANUAL_VERIFICATION_REQUIRED = "";
        public static final String ORDER_PARTIALLY_REFUNDED = "";

        public static final String ORDER_ready_to_pick = "Mới tạo đơn hàng";
        public static final String ORDER_picking = "Nhân viên đang lấy hàng";
        public static final String ORDER_cancel = "Hủy đơn hàng";
        public static final String ORDER_money_collect_picking = "Đang thu tiền người gửi";
        public static final String ORDER_picked = "Nhân viên đã lấy hàng";
        public static final String ORDER_storing = "Hàng đang nằm ở kho";
        public static final String ORDER_transporting = "Đang luân chuyển hàng";
        public static final String ORDER_sorting = "Đang phân loại hàng hóa";
        public static final String ORDER_delivering = "Nhân viên đang giao cho người nhận";
        public static final String ORDER_money_collect_delivering = "Nhân viên đang thu tiền người nhận";
        public static final String ORDER_delivered = "Nhân viên đã giao hàng thành công";
        public static final String ORDER_delivery_fail = "Nhân viên giao hàng thất bại";
        public static final String ORDER_waiting_to_return = "Đang đợi trả hàng về cho người gửi";
        public static final String ORDER_return = "Trả hàng";
        public static final String ORDER_return_transporting = "Đang luân chuyển hàng trả";
        public static final String ORDER_return_sorting = "Đang phân loại hàng trả";
        public static final String ORDER_returning = "Nhân viên đang đi trả hàng";
        public static final String ORDER_return_fail = "Nhân viên trả hàng thất bại";
        public static final String ORDER_returned = "Nhân viên trả hàng thành công";
        public static final String ORDER_exception = "Đơn hàng ngoại lệ không nằm trong quy trình";
        public static final String ORDER_damage = "Hàng bị hư hỏng";
        public static final String ORDER_lost = "Hàng bị mất";
    }

    private final String label;

    EOrderStatus(String label) {
        this.label = label;
    }

    public String toString() {
        return this.label;
    }

    // Pending — Customer started the checkout process but did not complete it.
    // Incomplete orders are
    // assigned a "Pending" status and can be found under the More tab in the View
    // Orders screen.
    // Awaiting Payment — Customer has completed the checkout process, but payment
    // has yet to be
    // confirmed. Authorize only transactions that are not yet captured have this
    // status.
    // Awaiting Fulfillment — Customer has completed the checkout process and
    // payment has been
    // confirmed.
    // Awaiting Shipment — Order has been pulled and packaged and is awaiting
    // collection from a
    // shipping provider.
    // Awaiting Pickup — Order has been packaged and is awaiting customer pickup
    // from a
    // seller-specified location.
    // Partially Shipped — Only some items in the order have been shipped.
    // Completed — Order has been shipped/picked up, and receipt is confirmed;
    // client has paid for
    // their digital product, and their file(s) are available for download.
    // Shipped — Order has been shipped, but receipt has not been confirmed; seller
    // has used the Ship
    // Items action. A listing of all orders with a "Shipped" status can be found
    // under the More tab
    // of the View Orders screen.
    // Cancelled — Seller has cancelled an order, due to a stock inconsistency or
    // other reasons.
    // Stock levels will automatically update depending on your Inventory Settings.
    // Cancelling an
    // order will not refund the order. This status is triggered automatically when
    // an order using an
    // authorize-only payment gateway is voided in the control panel before
    // capturing payment.
    // Declined — Seller has marked the order as declined.
    // Refunded — Seller has used the Refund action to refund the whole order. A
    // listing of all
    // orders with a "Refunded" status can be found under the More tab of the View
    // Orders screen.
    // Disputed — Customer has initiated a dispute resolution process for the PayPal
    // transaction that
    // paid for the order or the seller has marked the order as a fraudulent order.
    // Manual Verification Required — Order on hold while some aspect, such as
    // tax-exempt
    // documentation, is manually confirmed. Orders with this status must be updated
    // manually.
    // Capturing funds or other order actions will not automatically update the
    // status of an order
    // marked Manual Verification Required.
    // Partially Refunded — Seller has partially refunded the order.
}
