//package com.docease.mailing_service.service;
//
//import com.docease.mailing_service.config.ThymeleafConfig;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private ThymeleafConfig thymeleafConfig;
//
//    @Value("${app.email.enabled:true}")
//    private boolean emailEnabled;
//
//    @Value("${app.email.from}")
//    private String fromEmail;
//
//    @Async
//    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
//        if (!emailEnabled) {
//            //log.info("Email disabled. Would send {} to {}", subject, to);
//            return;
//        }
//
//        try {
//            Context context = new Context();
//            variables.forEach(context::setVariable);
//
//            String htmlContent = thymeleafConfig.templateEngine().process(templateName, context);
//
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true);
//
//            mailSender.send(message);
//            log.info("Sent email to {}: {}", to, subject);
//
//        } catch (Exception e) {
//            log.error("Failed to send email to {}: {}", to, e.getMessage());
//        }
//    }
//
//    // === Convenience Methods ===
//    public void sendAcknowledgement(User user, Appointment appointment) {
//        Map<String, Object> vars = Map.of(
//                "user", user,
//                "appointment", appointment,
//                "supportEmail", fromEmail
//        );
//        sendTemplateEmail(user.getEmail(), "Appointment Confirmed", "emails/acknowledgement", vars);
//    }
//
//    public void sendOtp(String email, String otp) {
//        Map<String, Object> vars = Map.of("otp", otp);
//        sendTemplateEmail(email, "Your OTP Code", "emails/otp", vars);
//    }
//
//
//}
