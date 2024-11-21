package com.avirantEnterprises.information_collector.service.login;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {
   @Autowired
    private JavaMailSender javaMailSender;
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for Login");
        message.setText("Your OTP is: " + otp);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();  // Log the error for debugging
        }
    }
}
