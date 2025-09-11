package com.lantz.lantzaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/10
 *
 * @author Lantz
 * @version 1.0
 * @Description TerminateTool
 * @since 1.8
 */
public class TerminateTool {

    @Tool(description = """
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.
            When you have finished all the tasks, call this tool to end the work.
            """)
    public String doTerminate() {
        return "任务结束！";
    }

}
