package com.lantz.lantzaiagent.rag.rewriter;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/26
 *
 * @author Lantz
 * @version 1.0
 * @Description QueryRewriter
 * @since 1.8
 */

/**
 * 查询重写器
 */
@Component
public class QueryRewriter {

    private final QueryTransformer queryTransformer;

    public QueryRewriter(ChatModel dashscopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        // 创建查询重写和翻译器
        queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    /**
     * 查询重写转换
     * @param prompt
     * @return
     */
    public String queryWriterTransformer(String prompt){
        Query query = new Query(prompt);
        // 执行查询重写
        Query transformedQuery = queryTransformer.transform(query);
        // 返回重写后的查询
        return transformedQuery.text();
    }
}
