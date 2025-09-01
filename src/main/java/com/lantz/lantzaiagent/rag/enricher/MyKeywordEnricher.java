package com.lantz.lantzaiagent.rag.enricher;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/25
 *
 * @author Lantz
 * @version 1.0
 * @Description MyKeywordEnricher
 * @since 1.8
 */
@Component
public class MyKeywordEnricher {

    @Resource
    private ChatModel dashscopeChatModel;


    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(this.dashscopeChatModel, 5);
        return enricher.apply(documents);
    }
}
