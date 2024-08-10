package com.example.ViChat.service;

import com.example.ViChat.dto.request.MailRequest;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendHtmlMail(MailRequest mailRequest, String templateName) throws MessagingException;
}
