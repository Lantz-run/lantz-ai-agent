package com.lantz.lantzaiagent.tools;

import com.lantz.lantzaiagent.LantzAiAgentApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/6
 *
 * @author Lantz
 * @version 1.0
 * @Description EmailSendingToolTest
 * @since 1.8
 */
@SpringBootTest
class EmailSendingToolTest {

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.port}")
    private int port;

    @Test
    void sendTextEmail() {
        EmailSendingTool tool = new EmailSendingTool(host, port, username, password);
        String subject = "恋爱计划";
        String toEmail = "1196046661@qq.com";
//        String content = "<h2 style='color:blue;'>这是一封测试邮件</h2><p>古巨基 - 情歌王</p>";
        String content = "这是一封恋爱约会计划邮件 lantz";
        String filename = "七夕约会计划.pdf";
        String result = tool.sendAttachEmail(subject, toEmail, content, filename);
        assertNotNull(result);

    }
}