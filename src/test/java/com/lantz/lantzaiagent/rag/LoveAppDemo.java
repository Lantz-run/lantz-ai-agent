package com.lantz.lantzaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/17
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveAppTest
 * @since 1.8
 */
@SpringBootTest
class LoveAppDemo {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadMarkdown() {
        loveAppDocumentLoader.loadMarkdown();
    }


}