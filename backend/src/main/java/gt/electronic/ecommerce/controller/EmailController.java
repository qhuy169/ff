package gt.electronic.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gt.electronic.ecommerce.models.EmailRequest;
import gt.electronic.ecommerce.services.EmailService;

@RestController
@RequestMapping("/api/v1/Mail")
@CrossOrigin(origins = "*") // Cho phép truy cập từ frontend
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    // API gửi email ngay lập tức
    @PostMapping("/RegularEmail")
    public ResponseEntity<String> sendRegularEmail(@RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return ResponseEntity.ok("Email sent successfully!");
    }

    // API lên lịch gửi email
    @PostMapping("/ScheduledEmail")
    public ResponseEntity<String> scheduleEmail(@RequestBody EmailRequest emailRequest) {
        if (emailRequest.getTimeToSend() == null) {
            return ResponseEntity.badRequest().body("Time to send is required for scheduled emails.");
        }
        emailService.scheduleEmail(emailRequest);
        return ResponseEntity.ok("Email scheduled successfully!");
    }
}
