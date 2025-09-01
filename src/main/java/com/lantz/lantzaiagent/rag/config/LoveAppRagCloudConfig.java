package com.lantz.lantzaiagent.rag.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/17
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveAppRagCloudConfig
 * @since 1.8
 */

/**
 * 初始化基于云知识库的检索增强顾问 Bean
 */
@Configuration
@Slf4j
public class LoveAppRagCloudConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashscopeApiKey;

    @Bean
    public Advisor loveAppRagCloudAdvisor(){
        DashScopeApi dashScopeApi = new DashScopeApi(dashscopeApiKey);
        final String KNOWLEDGE_BASE = "恋爱大师1.1";
        DocumentRetriever retriever = new DashScopeDocumentRetriever(dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder()
                        .withIndexName(KNOWLEDGE_BASE)
                        .build());
        return RetrievalAugmentationAdvisor
                .builder()
                .documentRetriever(retriever)
                .build();
    }
}
