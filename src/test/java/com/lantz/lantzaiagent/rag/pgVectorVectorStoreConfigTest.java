package com.lantz.lantzaiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/22
 *
 * @author Lantz
 * @version 1.0
 * @Description pgVectorVectorStoreConfigTest
 * @since 1.8
 */
@SpringBootTest
class pgVectorVectorStoreConfigTest {

    @Resource
    private VectorStore pgVectorVectorStore;

    @Test
    void pgVectorVectorStore() {

        List<Document> documents = List.of(
                new Document("lantz一定要努力努力啊，要成为伟大的画家编程师", Map.of("meta1", "meta1")),
                new Document("lantz 很想去自由地旅游"),
                new Document("今天的天气很晴朗啊", Map.of("meta2", "meta2")));

// Add the documents to PGVector
        pgVectorVectorStore.add(documents);

// Retrieve documents similar to a query
        List<Document> results = pgVectorVectorStore.similaritySearch(SearchRequest.builder().query("Spring").topK(5).build());
        Assertions.assertNotNull(results);
    }
}