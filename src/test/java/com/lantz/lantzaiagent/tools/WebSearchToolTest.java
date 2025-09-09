package com.lantz.lantzaiagent.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/1
 *
 * @author Lantz
 * @version 1.0
 * @Description WebSearchToolTest
 * @since 1.8
 */
@SpringBootTest
class WebSearchToolTest {

    @Value("${searchapi.apikey}")
    private String apikey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(apikey);
        String query = "程序员鱼皮编程导航 codefather.cn";
        String result = webSearchTool.searchWeb(query);
        assertNotNull(result);
    }
}