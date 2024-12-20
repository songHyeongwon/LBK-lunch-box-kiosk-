package com.example.lunchboxkiosk.schedule;

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

    @Scheduled(fixedRate = 5000)
    // @Scheduled(cron = "0 30 11 * * *")
    public void hsdMailingSchedule() {
        try {
            mailService.sendHsdOrdersByEmail();
        } catch (Exception e) {
            throw new RuntimeException("메일 발송 실패" + e.getMessage());
        }
        log.info("HSD order mail send success.");
    }
}
