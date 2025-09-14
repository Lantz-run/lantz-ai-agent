package com.lantz.lantzaiagent.controller;

import com.lantz.lantzaiagent.agent.LantzManus;
import com.lantz.lantzaiagent.annotation.AuthCheck;
import com.lantz.lantzaiagent.app.LoveApp;
import com.lantz.lantzaiagent.constant.UserConstant;
import com.lantz.lantzaiagent.model.entity.User;
import com.lantz.lantzaiagent.model.enums.LoveStateEnum;
import com.lantz.lantzaiagent.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/11
 *
 * @author Lantz
 * @version 1.0
 * @Description AiController
 * @since 1.8
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    @Resource
    private UserService userService;


    /**
     * 同步调用 ai 聊天
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/love_app/ai_chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId){
        return loveApp.doChat(message, chatId);
    }

    /**
     * 同步调用 ai 聊天（流式接口）
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/ai_chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message, String chatId, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        if (loginUserId == null) {
            return null;
        }
        return loveApp.doChatByStream(message, chatId);
    }


    /**
     * 同步调用 ai 聊天（流式接口）-- 基于rag知识库
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/ai_chat_rag/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithRAGLoveAppSSE(String message, String chatId, String state, HttpServletRequest request){
        if (LoveStateEnum.getEnumByValue(state) == null) {
            return null;
        }
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        if (loginUserId == null) {
            return null;
        }
        return loveApp.doChatWithRagByStream(message, chatId, state);
    }

    /**
     * 流式调用 ai 聊天 ServerSentEvent
     * @param message
     * @param chatId
     * @return
     */

    @GetMapping(value = "/love_app/ai_chat/serverSentEvent")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(String message, String chatId){
        return loveApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 流式调用 ai 聊天 ServerSentEvent
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/love_app/ai_chat/emitter")
    public SseEmitter doChatWithLoveAppSseEmitter(String message, String chatId){

        // 创建一个 2 分钟的 sseEmitter
        SseEmitter sseEmitter = new SseEmitter(120000L);

        // 获取 flux 直接订阅
        loveApp.doChatByStream(message, chatId)
                .subscribe(
                        chunk -> {
                            try {
                                sseEmitter.send(chunk);
                            } catch (Exception e) {
                                sseEmitter.completeWithError(e);
                            }
                        },
                        // 处理错误
                        sseEmitter::completeWithError,
                        // 处理完成
                        sseEmitter::complete
                );
        return sseEmitter;
    }

    /**
     * 流式调用 Manus 智能体
     * @param message 用户提示词
     * @return
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/manus/ai_chat")
    public SseEmitter doChatWithManus(String message){
        LantzManus lantzManus = new LantzManus(allTools, dashscopeChatModel);
        return lantzManus.runStream(message);
    }

}
