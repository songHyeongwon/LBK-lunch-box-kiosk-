package com.example.lunchboxkiosk.schedule;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CrawlingSchedulerTest {

    @Autowired
    private CrawlingScheduler crawlingScheduler;

    @Test
    public void testCrawlingScheduler() {
        crawlingScheduler.runCrawling();
    }
}