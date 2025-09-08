package com.lantz.lantzaiagent.rag.factory;

import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/26
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveAppContextualQueryAugmenterFactory
 * @since 1.8
 */

/**
 * 恋爱 RAG 自定义上下文查询增强工厂
 */
public class LoveAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyPromptTemplate = new PromptTemplate("""
                你必须添加输出下面内容：
                抱歉，我只能回复关于恋爱的问题，暂时无法回复您当前的问题哦
                如有疑问或者咨询问题，请联系1196046661@qq.com邮箱
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .emptyContextPromptTemplate(emptyPromptTemplate)
                .build();
    }
}
