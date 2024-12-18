package com.example.lunchboxkiosk.schedule;

import com.example.lunchboxkiosk.model.dto.HsdCategoryDto;
import com.example.lunchboxkiosk.service.HsdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final HsdService hsdService;
    private final ObjectMapper objectMapper;

    private static final String HSD_CRAWLER_SCRIPT = "hsd_crawler.py";
    private final URL resourcesUrl = getClass().getClassLoader().getResource("static/scripts");

    // @Scheduled(fixedRate = 5000)         // 5초마다 실행
    @Scheduled(cron = "0 */10 * * * *")     // 10분마다 실행
    // @Scheduled(cron = "0 0 10 * * *")    // 매일 오전 10시 실행
    public void hsdCrawling() {
        try {
            String script = Paths.get(this.resourcesUrl.toURI()).resolve(HSD_CRAWLER_SCRIPT).toString();

            // 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python", script);
            Process process = processBuilder.start();

            // 표준 출력 로그 읽기
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder jsonOutput = new StringBuilder();
            String line;
            while ((line = outputReader.readLine()) != null) {
                jsonOutput.append(line);
            }

            // JSON 데이터를 객체로 변환
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            List<HsdCategoryDto> categories = objectMapper.readValue(
                    jsonOutput.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HsdCategoryDto.class)
            );

            hsdService.saveCategories(categories);
            // hsdService.getCategories().forEach(System.out::println);

            // 에러 출력
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println("[ERROR] " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("HSD Crawler exited with code " + exitCode);
            }
            log.info("HSD Crawling success");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
