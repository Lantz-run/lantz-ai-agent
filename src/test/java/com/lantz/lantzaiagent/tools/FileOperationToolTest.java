package com.lantz.lantzaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/1
 *
 * @author Lantz
 * @version 1.0
 * @Description FileOperationToolTest
 * @since 1.8
 */
@SpringBootTest
class FileOperationToolTest {

    @Test
    void fileRead() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "够钟.txt";
        String result = tool.FileRead(fileName);
        assertNotNull(result);
        System.out.println(result);
    }

    @Test
    void fileWrite() {
        FileOperationTool tool = new FileOperationTool();
        String fileName = "够钟.txt";
        String content = "何时落到这收场，枯死在你的手上";
        String result = tool.FileWrite(fileName, content);
        assertNotNull(result);
    }
}