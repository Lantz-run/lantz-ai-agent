package com.lantz.lantzaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Value("classpath:/prompts/system-message.st")
    private org.springframework.core.io.Resource systemResource;

    @Test
    void testChat() {
        // 第一轮对话
        String message = "你好，我是小明！";
        String chatId = UUID.randomUUID().toString();
        String chat1 = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(chat1);
//        // 第二轮对话
//        message = "我的女友是小红，我和她发生了一些争吵";
//        String chat2 = loveApp.doChat(message, chatId);
//        Assertions.assertNotNull(chat2);
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
//        messages.add(new UserMessage("恐龙在什么时候灭绝的"));
//        chatResponse = chatModel.call(new Prompt(messages));
//        content = chatResponse.getResult().getOutput().getText();
//        Assertions.assertNotNull(content);
//        System.out.println(content);
    }

    @Test
    void testAiStructure(){
        // 第一轮对话
        String message = "你好，我是小明！我想和我的对象小红关系升温，但是我不知道怎么做";
        String chatId = UUID.randomUUID().toString();
        LoveApp.LoveReport loveReport = loveApp.doChatStructure(message, chatId);
        Assertions.assertNotNull(loveReport);

    }

    @Test
    void doChatWithRag() {
        String message = "到底谁是lantz啊！！！！";
        String chatId = UUID.randomUUID().toString();
        String content = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(content);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("帮我生成几张晚霞的照片");

        // 测试网页抓取：恋爱案例分析
//        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");
//
//        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");
//
//        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");
//
//        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文件");
//
//        // 测试 PDF 生成
//        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
//        String answer = loveApp.doChatWithTools(message, chatId);
        String answer = loveApp.doChatWithMCP(message, chatId);
        Assertions.assertNotNull(answer);
    }

}