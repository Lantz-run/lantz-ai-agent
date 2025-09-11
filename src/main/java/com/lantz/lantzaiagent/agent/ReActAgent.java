package com.lantz.lantzaiagent.agent;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/10
 *
 * @author Lantz
 * @version 1.0
 * @Description ReActAgent
 * @since 1.8
 */

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ReAct (Reasoning and Acting) 模式的代理抽象类
 * 实现了思考-行动的循环模式
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ReActAgent extends BaseAgent {

    /**
     * 处理当前状态并决定下一步行动1
     * @return 是否需要执行行动，true -- 执行，false -- 不执行
     */
    public abstract boolean think();

    /**
     * 执行决定的行动
     * @return 执行行动的结果
     */
    public abstract String act();

    @Override
    public String step() {

        try {
            boolean shouldAct = this.think();
            if (!shouldAct) {
                return "思考完成 -- 不需要行动";
            }
            return act();
        } catch (Exception e) {
            // 打印日志
            e.printStackTrace();
            return "步骤执行失败, " + e.getMessage();
        }
    }
}
