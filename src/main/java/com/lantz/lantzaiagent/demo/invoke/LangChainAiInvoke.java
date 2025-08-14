package com.lantz.lantzaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/7
 *
 * @author Lantz
 * @version 1.0
 * @Description LangChainAiInvoke
 * @since 1.8
 */
public class LangChainAiInvoke {
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestAiKey.API_KEY)
                .modelName("qwen-plus")
                .build();
        String chat = qwenChatModel.chat("你好，我是lantz，很高兴和你交流编程知识！");
        System.out.println(chat);
    }
}
