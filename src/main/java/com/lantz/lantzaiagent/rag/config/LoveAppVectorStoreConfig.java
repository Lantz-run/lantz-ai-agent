package com.lantz.lantzaiagent.rag.config;

import com.lantz.lantzaiagent.rag.enricher.MyKeywordEnricher;
import com.lantz.lantzaiagent.rag.LoveAppDocumentLoader;
import com.lantz.lantzaiagent.splitter.MyTokenTextSplitter;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/17
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveAppDocumentStore
 * @since 1.8
 */

/**
 * 恋爱 app 向量数据库配置（初始化基于内存的向量数据库 Bean）
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;

    @Resource
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    public VectorStore loveAppVectorStore(EmbeddingModel dashscopeEmbeddingModel){
        SimpleVectorStore simpleVectorStore = SimpleVectorStore
                .builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documentList = loveAppDocumentLoader.loadMarkdown();
        // 自主切分文档
//        List<Document> splitDocument = myTokenTextSplitter.splitCustomized(documentList);
        List<Document> enrichDocuments = myKeywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichDocuments);
        return simpleVectorStore;
    }
}
