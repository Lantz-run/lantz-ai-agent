package com.lantz.lantzaiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/7
 *
 * @author Lantz
 * @version 1.0
 * @Description SpringAiAiInvoke
 * @since 1.8
 */
//@Component
public class SpringAiAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;


    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好，我是lantz，很高兴和你交流编程知识！"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem("你是恋爱顾问")
                .build();
        String response = chatClient.prompt().user("你好啊！").call().content();
        System.out.println(response);
        ChatResponse chatResponse = chatClient.prompt().user("Tell me a joke")
                .call().chatResponse();

    }


}
