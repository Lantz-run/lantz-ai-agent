package com.lantz.lantzaiagent.model.enums;

import lombok.Getter;
import org.springframework.ai.chat.messages.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/13
 *
 * @author Lantz
 * @version 1.0
 * @Description MessageEnum
 * @since 1.8
 */
@Getter
public enum MessageTypeEnum {

    /**
     * A {@link Message} of type {@literal user}, having the user role and originating
     * from an end-user or developer.
     * @see UserMessage
     */
    USER("user", UserMessage.class),

    /**
     * A {@link Message} of type {@literal assistant} passed in subsequent input
     * {@link Message Messages} as the {@link Message} generated in response to the user.
     * @see AssistantMessage
     */
    ASSISTANT("assistant", AssistantMessage.class),

    /**
     * A {@link Message} of type {@literal system} passed as input {@link Message
     * Messages} containing high-level instructions for the conversation, such as behave
     * like a certain character or provide answers in a specific format.
     * @see SystemMessage
     */
    SYSTEM("system", SystemMessage.class),

    /**
     * A {@link Message} of type {@literal function} passed as input {@link Message
     * Messages} with function content in a chat application.
     * @see ToolResponseMessage
     */
    TOOL("tool", ToolResponseMessage.class);

    private final String value;

    private final Class<?> clazz;

    MessageTypeEnum(String value, Class<?> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public static MessageTypeEnum fromValue(String value) {
        for (MessageTypeEnum messageTypeEnum : MessageTypeEnum.values()){
            if (messageTypeEnum.getValue().equals(value)){
                return messageTypeEnum;
            }
        }
        throw new IllegalArgumentException("Invalid MessageType value: " + value);
    }

}
