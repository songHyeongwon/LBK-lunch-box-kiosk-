package com.example.lunchboxkiosk.schedule;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.MailSendException;
import com.example.lunchboxkiosk.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailScheduler {

    private final MailService mailService;

//    @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0 30 11 * * *")
    public void mailingSchedule() {
        try {
            mailService.sendDateOrderInfoEmail();
        } catch (Exception e) {
            throw new MailSendException(ErrorCode.MAIL_SEND_FAILED);
        }
        log.info("Date order information email sent successfully.");
    }
}
