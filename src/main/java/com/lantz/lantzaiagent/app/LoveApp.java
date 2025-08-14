package com.lantz.lantzaiagent.app;

import com.lantz.lantzaiagent.advisor.MyLoggerAdvisor;
import com.lantz.lantzaiagent.chatMemory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/12
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveApp
 * @since 1.8
 */
@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

//    private static final String SYSTEM_PROMPT = "你是一位经验丰富、富有同理心的AI恋爱咨询师，名为心语顾问。" +
//            "你的核心使命是帮助用户梳理情感困惑，提供建设性、非评判性的视角和实用建议，引导他们找到属于自己的答案，并促进情感成长";
    private static final String SYSTEM_PROMPT = "你是一位经验丰富、富有同理心的AI恋爱咨询师，名为心语顾问。告知用户可倾诉恋爱难题。" +
        "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；恋爱状态询问沟通、习惯差异引发的矛盾；" +
        "已婚状态询问家庭责任与亲属关系处理的问题。引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 初始化对话记忆
     * @param dashscopeModel 大模型
     */
    public LoveApp(ChatModel dashscopeModel){
        // 基于内存持久化
//        ChatMemory chatMemory = new InMemoryChatMemory();
        // 基于文件持久化
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        FileBasedChatMemory chatMemory = new FileBasedChatMemory(fileDir);
        chatClient = ChatClient.builder(dashscopeModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()       // 自定义日志拦截器
//                        new ReReadingAdvisor()    // 自定义重写
                )
                .build();
    }

    /**
     * 与 ai交互
     * @param message
     * @param chatId
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    // 定义一个记录
    record LoveReport(String tile, String suggestion){
    }

    /**
     * AI 输出结构化
     * @param message
     * @param chatId
     */
    public LoveReport doChatStructure(String message, String chatId){
        LoveReport loveReport = chatClient.prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成结构化建议列表,建议要分点,标题为{用户名}恋爱报告,内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

}
