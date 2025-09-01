package com.lantz.lantzaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

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
class LoveAppTest {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadMarkdown() {
        loveAppDocumentLoader.loadMarkdown();
    }


}