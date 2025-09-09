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
 * @Description PDFGenerationToolTest
 * @since 1.8
 */

@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generationPDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "编程导航原创项目.pdf";
        String content = "编程导航原创项目 https://www.codefather.cn";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }

}