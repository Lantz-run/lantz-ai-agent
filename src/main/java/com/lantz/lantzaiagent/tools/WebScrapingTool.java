package com.lantz.lantzaiagent.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.IOException;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/2
 *
 * @author Lantz
 * @version 1.0
 * @Description WebScrapingTool
 * @since 1.8
 */
public class WebScrapingTool {

    @Tool(description = "Scrape the content of web page")
    public String scrapeWebPage(@ToolParam(description = "The url of web page to scrape") String url){
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.html();
        } catch (IOException e) {
            return "Error scrape web page from the url" + e.getMessage();
        }
    }

}
