package com.agent.email.service;

import com.agent.email.config.GmailCredential;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Draft;
import com.google.api.services.gmail.model.Message;
import org.springframework.stereotype.Service;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.Base64;

@Service
public class GmailService {

    private final Gmail gmail;

    public GmailService(GmailCredential gmailCredential) {

        this.gmail = new Gmail.Builder(
                gmailCredential.getHttpTransport(),
                com.google.api.client.json.gson.GsonFactory
                        .getDefaultInstance(),
                gmailCredential.getCredential()
        )
                .setApplicationName("AI Email Agent")
                .build();
    }

    public String testConnection() throws Exception {

        return gmail.users()
                .getProfile("me")
                .execute()
                .getEmailAddress();
    }

    public String createDraft(
            String to,
            String subject,
            String body) throws Exception {

        MimeMessage email = createEmail(
                to,
                subject,
                body
        );

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        email.writeTo(buffer);

        byte[] rawMessageBytes =
                buffer.toByteArray();

        String encodedEmail =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(rawMessageBytes);

        Message message = new Message();

        message.setRaw(encodedEmail);

        Draft draft = new Draft();

        draft.setMessage(message);

        Draft createdDraft =
                gmail.users()
                        .drafts()
                        .create("me", draft)
                        .execute();

        return createdDraft.getId();
    }

    private MimeMessage createEmail(
            String to,
            String subject,
            String body) throws Exception {

        Properties props = new Properties();

        Session session =
                Session.getDefaultInstance(
                        props,
                        null
                );

        MimeMessage email =
                new MimeMessage(session);

        email.setFrom(
                new InternetAddress(
                        gmail.users()
                                .getProfile("me")
                                .execute()
                                .getEmailAddress()
                )
        );

        email.addRecipient(
                jakarta.mail.Message.RecipientType.TO,
                new InternetAddress(to)
        );

        email.setSubject(subject, "UTF-8");

        email.setText(body, "UTF-8");

        return email;
    }
}