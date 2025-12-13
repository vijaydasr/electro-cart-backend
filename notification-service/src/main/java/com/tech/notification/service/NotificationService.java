package com.tech.notification.service;

import com.tech.orderservice.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listenOrderPlaced(OrderEvent orderEvent) {
        log.info("Received notification for order placed {}", orderEvent);
        // Send email/SMS notification
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("springshop@email.com");
            mimeMessageHelper.setTo(orderEvent.getEmailId());
            mimeMessageHelper.setSubject(String.format("Order Placed Successfully - #%s", orderEvent.getOrderId()));
            mimeMessageHelper.setText(String.format("""
            Hi,
            
            Your order with order id %s has been placed successfully.
            
            Thank you for shopping with us.
            
            Regards,
            Spring Shop Team
            """, orderEvent.getOrderId()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Notification email sent successfully to {}", orderEvent.getEmailId());
        } catch (MailException e) {
            log.error("Failed to send notification email to {}", orderEvent.getEmailId(), e);
            throw new RuntimeException("Failed to send email notification ", e);
        }
    }
}
