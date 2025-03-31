package gt.electronic.ecommerce.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailRequest {
    private String to;
    private String subject;
    private String body;
    private LocalDateTime timeToSend; // Null nếu gửi ngay
}