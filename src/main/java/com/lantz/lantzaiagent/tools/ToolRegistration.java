package com.lantz.lantzaiagent.tools;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/3
 *
 * @author Lantz
 * @version 1.0
 * @Description ToolRegistration
 * @since 1.8
 */

@Configuration
public class ToolRegistration {

    @Value("${searchapi.apikey}")
    private String apiKey;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Bean
    public ToolCallback[] allTools(){
        FileOperationTool fileOperationTool = new FileOperationTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        TerminateTool terminateTool = new TerminateTool();
        EmailSendingTool emailSendingTool = new EmailSendingTool(host, port, username, password, protocol);

        return ToolCallbacks.from(
                fileOperationTool,
                pdfGenerationTool,
//                resourceDownloadTool,
//                terminalOperationTool,
//                webScrapingTool,
//                webSearchTool,
                terminateTool,
                emailSendingTool
        );
    }

}
