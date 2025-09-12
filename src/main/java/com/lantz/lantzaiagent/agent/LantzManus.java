package com.lantz.lantzaiagent.agent;

import com.lantz.lantzaiagent.advisor.ProhibitedAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/10
 *
 * @author Lantz
 * @version 1.0
 * @Description LantzManus
 * @since 1.8
 */
@Component
public class LantzManus extends ToolCallAgent{

    public LantzManus(ToolCallback[] allTools, ChatModel dashscopeChatModel) {
        super(allTools);
        setName("lantzManus");
        String SYSTEM_PROMPT = """
                You are OpenManus, an all-capable AI assistant, aimed at solving any task presented by the user. 
                You have various tools at your disposal that you can call upon to efficiently complete complex requests. 
                """;
        this.setSystemPrompt(SYSTEM_PROMPT);
        String NEXT_STEP_PROMPT = """
                Based on the current state and available tools, what should be done next?
                Think step by step about the problem and identify which MCP tool would be most helpful for the current stage.
                If you've already made progress, consider what additional information you need or what actions would move 
                you closer to completing the task.
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);
        this.setMaxStep(10);
        // 初始化客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new ProhibitedAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
