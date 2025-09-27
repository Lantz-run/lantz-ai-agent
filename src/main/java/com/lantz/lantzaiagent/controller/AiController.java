package com.lantz.lantzaiagent.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lantz.lantzaiagent.agent.LantzManus;
import com.lantz.lantzaiagent.annotation.AuthCheck;
import com.lantz.lantzaiagent.app.LoveApp;
import com.lantz.lantzaiagent.common.BaseResponse;
import com.lantz.lantzaiagent.common.ResultUtils;
import com.lantz.lantzaiagent.constant.UserConstant;
import com.lantz.lantzaiagent.dao.ConversationMemoryDAO;
import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import com.lantz.lantzaiagent.exception.ThrowUtils;
import com.lantz.lantzaiagent.model.dto.ai.ConversationMemoryQueryRequest;
import com.lantz.lantzaiagent.model.entity.ConversationMemory;
import com.lantz.lantzaiagent.model.entity.User;
import com.lantz.lantzaiagent.model.enums.LoveStateEnum;
import com.lantz.lantzaiagent.model.vo.UserVO;
import com.lantz.lantzaiagent.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

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

    @Resource
    private ConversationMemoryDAO conversationMemoryDAO;


    /**
     * 同步调用 ai 聊天
     * @param message
     * @return
     */
    @GetMapping("/love_app/ai_chat/sync")
    public String doChatWithLoveAppSync(String message,
                                        @RequestParam(required = false) String conversationId){
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
            ThrowUtils.throwIf(conversationMemoryDAO.isConversationIdExist(conversationId), ErrorCode.PARAMS_ERROR, "该会话已存在");
        }
        return loveApp.doChat(message, conversationId);
    }

    /**
     * 同步调用 ai 聊天（流式接口）
     * @param message
     * @param conversationId
     * @return
     */
    @GetMapping(value = "/love_app/ai_chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSSE(String message,
                                             @RequestParam(required = false) String conversationId,
                                             HttpServletRequest request){
        this.checkUserAndConversation(conversationId, request);
        return loveApp.doChatByStream(message, conversationId);
    }


    /**
     * 同步调用 ai 聊天（流式接口）-- 基于rag知识库
     * @param message
     * @param conversationId
     * @return
     */
    @GetMapping(value = "/love_app/ai_chat_rag/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithRAGLoveAppSSE(String message,
                                                @RequestParam(required = false) String conversationId,
                                                String state,
                                                HttpServletRequest request){
        if (LoveStateEnum.getEnumByValue(state) == null) {
            return null;
        }
        this.checkUserAndConversation(conversationId, request);
        return loveApp.doChatWithRagByStream(message, conversationId, state);
    }

    /**
     * 检查用户登录和当前会话
     * @param conversationId
     * @param request
     */
    private void checkUserAndConversation(String conversationId, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        if (loginUserId == null) {
            return;
        }
        if (conversationId == null || conversationId.isEmpty()) {
            conversationId = UUID.randomUUID().toString();
            ThrowUtils.throwIf(conversationMemoryDAO.isConversationIdExist(conversationId), ErrorCode.PARAMS_ERROR, "该会话已存在");
        }
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

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ConversationMemory>> listConversationMemoryByPage(
            @RequestBody ConversationMemoryQueryRequest conversationMemoryQueryRequest,
                                                                   HttpServletRequest request) {
        if (conversationMemoryQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = conversationMemoryQueryRequest.getCurrent();
        long size = conversationMemoryQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ConversationMemory> conversationMemoryPage = conversationMemoryDAO.page(new Page<>(current, size),
                conversationMemoryDAO.getQueryWrapper(conversationMemoryQueryRequest));
        Page<ConversationMemory> conversationMemoryPage1 = new Page<>(current, size, conversationMemoryPage.getTotal());
        List<ConversationMemory> conversationMemory = conversationMemoryDAO.getConversationMemory(conversationMemoryPage.getRecords());
        conversationMemoryPage1.setRecords(conversationMemory);

        return ResultUtils.success(conversationMemoryPage1);
    }
}
