package com.example.lunchboxkiosk.schedule;

import com.example.lunchboxkiosk.model.dto.hsd.HsdCategoryDto;
import com.example.lunchboxkiosk.service.HsdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

    private String getScriptPath(String filename) throws URISyntaxException {
        if (resourcesUrl == null) {
            throw new RuntimeException("Url is null");
        }

        return Paths.get(this.resourcesUrl.toURI()).resolve(filename).toString();
    }

    private String fetchProcessOutputAsString(Process process) throws IOException {
        BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder jsonOutput = new StringBuilder();
        String line;
        while ((line = outputReader.readLine()) != null) {
            jsonOutput.append(line);
        }

        return jsonOutput.toString();
    }


    private void checkProcessError(Process process) throws IOException, InterruptedException {
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = errorReader.readLine()) != null) {
            System.err.println("[ERROR] " + line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Crawler exited with code " + exitCode);
        }
    }

//    @Scheduled(fixedRate = 5000)            // 5초마다 실행
    @Scheduled(cron = "0 */10 * * * *")     // 10분마다 실행
//    @Scheduled(cron = "0 0 10 * * *")       // 매일 오전 10시 실행
    public void hsdCrawlingSchedule() {
        try {
            // 프로세스 실행
            String script = getScriptPath(HSD_CRAWLER_SCRIPT);
            ProcessBuilder processBuilder = new ProcessBuilder("python", script);
            Process process = processBuilder.start();
            
            // 프로세스 출력 결과를 String(Json)으로 반환하고 List<HsdCategoryDto>로 변환하여 Redis에 저장
            String processOutput = fetchProcessOutputAsString(process);
            List<HsdCategoryDto> categories = objectMapper.readValue(
                    processOutput,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HsdCategoryDto.class)
            );
            hsdService.saveCategories(categories);

            // 에러 발생 여부 체크
            checkProcessError(process);
            log.info("HSD crawling success");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
