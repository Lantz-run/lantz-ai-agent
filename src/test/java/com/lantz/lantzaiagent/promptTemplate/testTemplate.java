package com.lantz.lantzaiagent.promptTemplate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/15
 *
 * @author Lantz
 * @version 1.0
 * @Description testTemplate
 * @since 1.8
 */
@SpringBootTest
public class testTemplate {

    @jakarta.annotation.Resource
    private ChatModel chatModel;

    /**
     * 直接在代码传入系统提示词模板
     */

    @Test
    void testPromptTemplate() {
        String userText = "我是小明，我最近和对象老是吵架";
        Message userMessage = new UserMessage(userText);

        String systemText = "你是一名{name}, 擅长{statement}";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(
                Map.of("name", "恋爱心语顾问",
                        "statement",
                        "帮助用户梳理情感困惑，提供建设性、非评判性的视角和实用建议，引导他们找到属于自己的答案，并促进情感成长")
        );
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        List<Generation> response = chatModel.call(prompt).getResults();
        Assertions.assertNotNull(response);
    }

    @Value("classpath:/prompts/system-message.st")
    private Resource systemResource;

    @Test
    void testLocalPromptTemplate() {
        String userText = "我是小明，我最近和对象老是吵架";
        Message userMessage = new UserMessage(userText);

//        String systemText = "你是一名{name}, 擅长{statement}";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
        Message systemMessage = systemPromptTemplate.createMessage(
                Map.of("attributes", "经验丰富、富有同理心",
                        "identity", "AI 恋爱咨询师",
                        "name", "心语顾问",
                        "invitation", "告诉用户可倾诉情感问题",
                        "single", "单身状态：你目前在拓展社交圈或追求心仪对象时，遇到了哪些具体困扰?",
                        "relationship", "恋爱状态：你们因沟通方式或习惯差异引发了哪些矛盾?",
                        "married", "已婚状态：你在处理家庭责任或亲属关系时面临哪些挑战?",
                        "details", "引导用户详述事情经过、对方反应和自身想法",
                        "solution", "我将据此给出专属的解决方案")
        );
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        System.out.println(systemMessage.getText());
        List<Generation> response = chatModel.call(prompt).getResults();
        Assertions.assertNotNull(response);
        System.out.println(response);
    }

}
