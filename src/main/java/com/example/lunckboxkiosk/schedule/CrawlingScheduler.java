package com.example.lunckboxkiosk.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private static final String HSD_CRAWLER_SCRIPT = "hsd_crawler.py";
    private final URL resourcesUrl;

    public CrawlingScheduler() {
        this.resourcesUrl = getClass().getClassLoader().getResource("static/scripts");
        if (resourcesUrl == null) {
            throw new RuntimeException("Resource path not found: static/scripts");
        }
    }

    // @Scheduled(fixedRate = 5000)  // 5초마다 실행
    @Scheduled(cron = "0 */10 * * * *")   // 10분마다 실행
    // @Scheduled(cron = "0 0 10 * * *")    // 매일 오전 10시 실행
    public void hsdCrawling() {
        try {
            String script = Paths.get(this.resourcesUrl.toURI()).resolve(HSD_CRAWLER_SCRIPT).toString();

            // 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", script);
            Process process = processBuilder.start();

            // 표준 출력 로그 읽기
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = outputReader.readLine()) != null) {
                System.out.println("[OUTPUT] " + line);
            }

            // 표준 에러 로그 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println("[ERROR] " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
