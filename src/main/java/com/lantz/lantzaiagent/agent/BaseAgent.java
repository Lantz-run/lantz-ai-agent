package com.lantz.lantzaiagent.agent;

import cn.hutool.core.util.StrUtil;
import com.lantz.lantzaiagent.agent.model.AgentState;
import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/10
 *
 * @author Lantz
 * @version 1.0
 * @Description BaseAgent
 * @since 1.8
 */

/**
 * 抽象基础代理类，用于管理代理状态和执行流程。
 *
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能。
 *
 * 子类必须实现step方法。
 */
@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;

    // 提示
    private String systemPrompt;
    private String nextStepPrompt;

    // 状态
    private AgentState state = AgentState.IDLE;

    // 执行控制
    private int maxStep = 10;
    private int currentStep = 0;

    // LLM
    private ChatClient chatClient;

    // 对话记忆
    private List<Message> messageList = new ArrayList<>();


    /**
     * 运行代理
     * @param userPrompt 用户提示词
     * @return
     */
    public String run(String userPrompt){
        if (this.state != AgentState.IDLE) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Cannot run agent from state: " + this.state);
        }
        if (StrUtil.isBlank(userPrompt)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Cannot run agent with empty prompt");
        }
        // 更改状态
        state = AgentState.RUNNING;
        // 记录上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();

        try {
            for (int i = 0; i < maxStep && state != AgentState.FINISHED; i++) {
                int stepNums = i + 1;
                currentStep = stepNums;
                log.info("Executing step {}/{}", stepNums, maxStep);

                // 单步执行
                String stepResult = step();
                String result = "step " + stepNums + ": " + stepResult;
                results.add(result);
            }
            // 检查是否超出步数限制
            if (currentStep >= maxStep) {
                state = AgentState.FINISHED;
                results.add("Terminated: Reached max step (" + maxStep + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
            log.error("Executing agent error", e);
            return "执行错误, " + e.getMessage();
        } finally {
            this.cleanUp();
        }
    }

    /**
     * 流式运行代理
     * @param userPrompt 用户提示词
     * @return
     */
    public SseEmitter runStream(String userPrompt){

        // 设置超时时间: 4min
        SseEmitter emitter = new SseEmitter(240000L);

        // 异步调用执行
        CompletableFuture.runAsync(() -> {
            try {
                if (this.state != AgentState.IDLE) {
                    emitter.send("错误: 无法从该状态执行代理 -> " + this.state);
                    emitter.complete();
                    return;
                }
                if (StrUtil.isBlank(userPrompt)) {
                    emitter.send("错误: 不能使用空的用户提示词!!");
                    emitter.complete();
                    return;
                }
                // 更改状态
                state = AgentState.RUNNING;
                // 记录上下文
                messageList.add(new UserMessage(userPrompt));
                // 保存结果列表
                List<String> results = new ArrayList<>();

                try {
                    for (int i = 0; i < maxStep && state != AgentState.FINISHED; i++) {
                        int stepNums = i + 1;
                        currentStep = stepNums;
                        log.info("Executing step {}/{}", stepNums, maxStep);

                        // 单步执行
                        String stepResult = step();
                        String result = "step " + stepNums + ": " + stepResult;
                        results.add(result);
                        // 发送每一步的结果
                        emitter.send(result);
                    }
                    // 检查是否超出步数限制
                    if (currentStep >= maxStep) {
                        state = AgentState.FINISHED;
                        results.add("Terminated: Reached max step (" + maxStep + ")");
                    }
                    // 完成执行
                    emitter.complete();
                } catch (Exception e) {
                    state = AgentState.ERROR;
                    log.error("Executing agent error", e);
                    try {
                        emitter.send("执行智能体失败");
                        emitter.complete();
                    } catch (IOException ex) {
                        emitter.completeWithError(ex);
                    }
                } finally {
                    this.cleanUp();
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        //设置最大完成时间和回调
        emitter.onTimeout(() -> {
            this.state = AgentState.ERROR;
            this.cleanUp();
            log.error("SSE connection time out");
        });

        emitter.onCompletion(() -> {
            if (this.state == AgentState.RUNNING) {
                this.state = AgentState.FINISHED;
            }
            this.cleanUp();
            log.info("SSE connection completed");
        });

        return emitter;
    }


    /**
     * 单步执行
     * @return
     */
    public abstract String step();


    /**
     * 清理资源
     */
    protected void cleanUp() {
        // 子类可以重写这个方法
    }

}
