package com.facebook.service;

import lombok.extern.log4j.Log4j2;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class EmailHandlerService {

    private final static int MAX_ATTEMPTS = 3;

    @Value("${email.host}")
    private String host;

    @Value("${email.port}")
    private int port;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;

    @Retryable(retryFor = MailException.class, maxAttempts = MAX_ATTEMPTS, backoff = @Backoff(delay = 10000))
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
        }
    }

    // Метод автоматично викликається після того,
    // як досягнуто максимальної кількості спроб надсилання листа.
    @Recover
    public void handleMailException(MailException e, String to, String subject, String messageContent) {
        log.error("Не вдалося надіслати листа після {} спроб: ", MAX_ATTEMPTS, e);

    }

}
