package com.lantz.lantzaiagent.app;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/12
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveAppTest
 * @since 1.8
 */
@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ChatModel chatModel;

    @Test
    void testChat() {
        // 第一轮对话
        String message = "你好，我是小明！";
        String chatId = UUID.randomUUID().toString();
        String chat1 = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(chat1);
        // 第二轮对话
        message = "我的女友是小红，我和她发生了一些争吵";
        String chat2 = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(chat2);
        // 第三轮对话
//        message = "我的女友是谁？我忘了";
//        String chat3 = loveApp.doChat(message, chatId);
//        Assertions.assertNotNull(chat3);
    }

    @Test
    void testChatAi(){
        List<Message> messages = new ArrayList<>();
        // 第一轮对话
        messages.add(new SystemMessage("你是一名考古学家"));
        messages.add(new UserMessage("活化石是什么"));
        ChatResponse chatResponse = chatModel.call(new Prompt(messages));
        String content = chatResponse.getResult().getOutput().getText();
        Assertions.assertNotNull(content);
        System.out.println(content);
        // 第二轮对话
        messages.add(new UserMessage("恐龙在什么时候灭绝的"));
        chatResponse = chatModel.call(new Prompt(messages));
        content = chatResponse.getResult().getOutput().getText();
        Assertions.assertNotNull(content);
        System.out.println(content);
    }

    @Test
    void testAiStructure(){
        // 第一轮对话
        String message = "你好，我是小明！我想和我的对象小红关系升温，但是我不知道怎么做";
        String chatId = UUID.randomUUID().toString();
        LoveApp.LoveReport loveReport = loveApp.doChatStructure(message, chatId);
        Assertions.assertNotNull(loveReport);

    }

}