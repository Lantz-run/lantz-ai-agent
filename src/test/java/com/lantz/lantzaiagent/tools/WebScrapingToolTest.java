package com.lantz.lantzaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/2
 *
 * @author Lantz
 * @version 1.0
 * @Description WebScrapingToolTest
 * @since 1.8
 */
@SpringBootTest
class WebScrapingToolTest {

    @Test
    void scrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.codefather.cn";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }

}