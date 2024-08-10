package com.example.ViChat.service.impl;

import com.example.ViChat.dto.request.MailRequest;
import com.example.ViChat.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendHtmlMail(MailRequest mailRequest, String templateName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        Context context = new Context();
        context.setVariables(mailRequest.getProps());
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailRequest.getTo());
        helper.setSubject(mailRequest.getSubject());
        helper.setText(html, true);
        javaMailSender.send(message);
    }
}
