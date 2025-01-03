package com.example.lunchboxkiosk.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;


    private static final String HSD_MAIL_FROM = "noreply@accounts.gmail.com";
    private static final String HSD_MAIL_TO = "bumin@digicaps.com";
    private static final String HSD_MAIL_SUBJECT = "[LBK] 도시락 주문 목록";

    private String formatPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(price) + "원";
    }

    private String getHsdEmailContent() {
        return null;
    }

    public void sendHsdOrdersByEmail() {
        String content = getHsdEmailContent();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(HSD_MAIL_FROM, "Google Accounts <noreply@accounts.gmail.com>");
            helper.setTo(HSD_MAIL_TO);
            helper.setSubject(HSD_MAIL_SUBJECT);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

