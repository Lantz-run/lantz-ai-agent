package com.lantz.lantzaiagent.app;

import com.lantz.lantzaiagent.advisor.ProhibitedAdvisor;
import com.lantz.lantzaiagent.chatMemory.FileBasedChatMemory;
import com.lantz.lantzaiagent.chatMemory.MySQLBaseChatMemory;
import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import com.lantz.lantzaiagent.rag.factory.LoveAppContextualQueryAugmenterFactory;
import com.lantz.lantzaiagent.rag.factory.LoveAppRagCustomAdvisorFactory;
import com.lantz.lantzaiagent.rag.rewriter.QueryRewriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

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

    private final String systemPrompt;

    @jakarta.annotation.Resource
    private VectorStore loveAppVectorStore;

    @jakarta.annotation.Resource
    private Advisor loveAppRagCloudAdvisor;

    @jakarta.annotation.Resource
    private QueryRewriter queryRewriter;

    @jakarta.annotation.Resource
    private ToolCallback[] allTools;

    @jakarta.annotation.Resource
    private ToolCallbackProvider toolCallbackProvider;

//    private static final String SYSTEM_PROMPT = "你是一位经验丰富、富有同理心的AI恋爱咨询师，名为心语顾问。告知用户可倾诉恋爱难题。" +
//        "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；恋爱状态询问沟通、习惯差异引发的矛盾；" +
//        "已婚状态询问家庭责任与亲属关系处理的问题。引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    /**
     * 获取系统提示词文本
     *
     * @return
     */
    private String systemPrompt(Resource systemResource) {
        Message systemMessage = null;
        try {
            if (systemResource != null && systemResource.exists()) {
                SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
                systemMessage = systemPromptTemplate.createMessage(
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
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提示模板生成错误");
        }
        return systemMessage.getText();
    }

    /**
     * 初始化对话记忆
     * @param dashscopeModel 大模型
     * @param systemResource 系统提示词模板
     * @param chatMemory 基于 MySQL对话记忆
     */
    public LoveApp(ChatModel dashscopeModel,
                   @Value("classpath:prompts/system-message.st") Resource systemResource,
                   MySQLBaseChatMemory chatMemory) {
        // 构造函数中处理系统提示词
        this.systemPrompt = systemPrompt(systemResource);
        // 基于内存持久化
//        ChatMemory chatMemory1 = new InMemoryChatMemory();
        // 基于文件持久化
//        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
//        FileBasedChatMemory fileChatMemory = new FileBasedChatMemory(fileDir);

        chatClient = ChatClient.builder(dashscopeModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
//                        new MyLoggerAdvisor(),      // 自定义日志拦截器
                        new ProhibitedAdvisor()     // 违禁词拦截器
//                        new ReReadingAdvisor()    // 自定义重写拦截器
                )
                .build();
    }

    /**
     * 与 ai交互
     *
     * @param message
     * @param chatId
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new ProhibitedAdvisor())
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    /**
     * 响应式对象
     *
     * @param message
     * @param chatId
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new ProhibitedAdvisor())
                .stream()
                .content();
    }

    // 定义一个记录
    record LoveReport(String tile, String suggestion) {
    }

    /**
     * AI 输出结构化
     *
     * @param message
     * @param chatId
     */
    public LoveReport doChatStructure(String message, String chatId) {
        LoveReport loveReport = chatClient.prompt()
                .system(systemPrompt + "每次对话后都要生成结构化建议列表,建议要分点,标题为{用户名}恋爱报告,内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new ProhibitedAdvisor())
                .call()
                .entity(LoveReport.class);
        log.info("loveReport: {}", loveReport);
        return loveReport;
    }

    /*
        RAG 知识库
    */
    public String doChatWithRag(String message, String chatId, String state) {
        // 查询重写
        String reWriteMessage = queryRewriter.queryWriterTransformer(message);
        ChatResponse chatResponse = chatClient.prompt()
                .user(reWriteMessage) // 注入重写后的文本
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore)) // 应用知识库回答
                .advisors(loveAppRagCloudAdvisor) // 应用云知识库
                .advisors(
                        new ProhibitedAdvisor() // 违禁词拦截器
//                        new MyLoggerAdvisor() // 日志拦截器
                )
                .advisors(
                        LoveAppRagCustomAdvisorFactory
                                .createLoveAppRagCustomAdvisor(loveAppVectorStore, state)
                ) // 文档检索增强
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("RAGContent: {}", content);
        return content;
    }

    /**
     * 流式输出
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatWithRagByStream(String message, String chatId, String status) {
        // 查询重写
        String reWriteMessage = queryRewriter.queryWriterTransformer(message);
        return chatClient.prompt()
                .user(reWriteMessage) // 注入重写后的文本
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(loveAppRagCloudAdvisor) // 应用云知识库
                .advisors(
                        new ProhibitedAdvisor() // 违禁词拦截器
                )
                .advisors(
                        LoveAppRagCustomAdvisorFactory
                                .createLoveAppRagCustomAdvisor(loveAppVectorStore, status)
                ) // 文档检索增强
                .stream()
                .content();
    }


    /**
     * AI 调用工具
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new ProhibitedAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("AIToolContent: {}", content);
        return content;

    }

    /**
     * AI 调用 MCP 工具
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithMCP(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new ProhibitedAdvisor())
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("MCPContent: {}", content);
        return content;

    }

}
