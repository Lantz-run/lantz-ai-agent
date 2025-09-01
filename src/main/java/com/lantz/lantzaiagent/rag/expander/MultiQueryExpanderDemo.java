package com.lantz.lantzaiagent.rag.expander;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/25
 *
 * @author Lantz
 * @version 1.0
 * @Description QueryExpander
 * @since 1.8
 */
@Component
public class MultiQueryExpanderDemo {

    @Resource
    private ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    public List<Query> queryExpander(String query){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query(query));
        return queries;
    }

}
