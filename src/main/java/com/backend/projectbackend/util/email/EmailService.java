package com.backend.projectbackend.util.email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String email, String name, String token) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admin@ClockTrain.com", "ClockTrain");
        helper.setTo(email);
        helper.setSubject("ClockTrain - Confirm your email");

        String htmlContent = "<p>Hello: <strong>" + name + "</strong>,</p>"
                + "<p>Please confirm your email by clicking the link below:</p>"
                + "<p><a href=\"" + frontendUrl + "/auth/confirm-account\">Confirm email</a></p>"
                + "<p>And use the following code: <b>" + token + "</b></p>"
                + "<p>This token will expire in 1 hour.</p>";

        helper.setText(htmlContent, true); // true = HTML content

        mailSender.send(message);

        System.out.println("Confirmation email sent to: " + email);
    }

    public void sendResetPasswordEmail(String email, String name, String token) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admin@ClockTrain.com", "ClockTrain");
        helper.setTo(email);
        helper.setSubject("ClockTrain - Reset your password");

        String htmlContent = "<p>Hello: <strong>" + name + "</strong>,</p>"
                + "<p>We received a request to reset your password.</p>"
                + "<p>Please click the link below to reset it:</p>"
                + "<p><a href=\"" + frontendUrl + "/auth/reset-password\">Reset Password</a></p>"
                + "<p>And use the following reset code: <b>" + token + "</b></p>"
                + "<p>This token will expire in 1 hour.</p>";

        helper.setText(htmlContent, true); // true = HTML content

        mailSender.send(message);

        System.out.println("Password reset email sent to: " + email);
    }
}