package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.*;
import gt.electronic.ecommerce.models.EmailRequest;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final List<EmailRequest> scheduledEmails = new ArrayList<>();

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Gửi email ngay lập tức
    public void sendEmail(EmailRequest emailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getTo());
        message.setSubject(emailRequest.getSubject());
        message.setText(emailRequest.getBody());
        mailSender.send(message);
    }

    // Lưu email vào danh sách chờ gửi
    public void scheduleEmail(EmailRequest emailRequest) {
        scheduledEmails.add(emailRequest);
    }

    // Cron job chạy mỗi phút để kiểm tra email cần gửi
    @Scheduled(fixedRate = 60000)
    public void checkAndSendScheduledEmails() {
        List<EmailRequest> toSend = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (EmailRequest email : scheduledEmails) {
            if (email.getTimeToSend().isBefore(now) || email.getTimeToSend().isEqual(now)) {
                sendEmail(email);
                toSend.add(email);
            }
        }

        scheduledEmails.removeAll(toSend);
    }
}
