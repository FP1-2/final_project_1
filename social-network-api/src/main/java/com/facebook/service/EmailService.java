package com.facebook.service;

import lombok.extern.log4j.Log4j2;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmailService {

    @Value("${email.host}")
    private String host;
    @Value("${email.port}")
    private int port;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    public void sendEmail(String to, String subject, String messageContent) throws Exception {
        Email email = EmailBuilder.startingBlank()
                .from(username)
                .to(to)
                .withSubject(subject)
                .withHTMLText(messageContent)
                .buildEmail();
        try (Mailer mailer = MailerBuilder
                .withSMTPServer(host, port, username, password)
                .withTransportStrategy(TransportStrategy.SMTPS)  // SMTPS = SMTP + SSL
                .buildMailer()) {

            mailer.sendMail(email);
        } catch (MailException e) {
            log.error("Ошибка при отправке письма: ", e);
            // Для повторної спроби надсилання листа:
            // retrySendEmail(to, subject, messageContent);
        }
    }

    public void sendResetPasswordEmail(String email, String token, String url) throws Exception {
        String RESET_PASSWORD_LETTER_SUBJECT = "Reset password";
        String RESET_PASSWORD_LETTER_Content ="<p>Click the link below to reset your password:<br>"
                +"<a href=%s>Reset password</a>"
                +"<br>This link is valid for 15 minutes.<br>"
                +"If you didn't request password change just ignore this letter.</div>";
        sendEmail(email, RESET_PASSWORD_LETTER_SUBJECT,
                String.format(RESET_PASSWORD_LETTER_Content, url +"/"+ token +"?em=" + email));
    }
}
