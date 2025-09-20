package com.lantz.lantzaiagent.chatMemory;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.gson.Gson;
import com.lantz.lantzaiagent.dao.ConversationMemoryDAO;
import com.lantz.lantzaiagent.model.entity.ConversationMemory;
import com.lantz.lantzaiagent.model.enums.MessageTypeEnum;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/13
 *
 * @author Lantz
 * @version 1.0
 * @Description MySQLBaseChatMemory 基于 MySQL对话持久化
 * @since 1.8
 */
@Component
public class MySQLBaseChatMemory implements ChatMemory {

    private final ConversationMemoryDAO conversationMemoryDAO;

    private final Gson gson = new Gson();

    public MySQLBaseChatMemory(ConversationMemoryDAO conversationMemoryDAO) {
        this.conversationMemoryDAO = conversationMemoryDAO;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        // 1. 将消息转换为持久化对象（与内存版的addAll对应）
        List<ConversationMemory> conversationMemories = messages.stream()
                .map(message -> ConversationMemory.builder()
                        .conversationId(conversationId)
                        .type(message.getMessageType().getValue())
                        .memory(gson.toJson(message))
                        // 建议添加创建时间，用于排序和模拟内存列表的顺序
                        .createdTime(new Date())
                        .build())
                .collect(Collectors.toList());

        // 2. 批量保存（相当于内存版的addAll操作）
        if (!conversationMemories.isEmpty()) {
            conversationMemoryDAO.saveOrUpdateBatch(conversationMemories);
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 1. 查询该对话的所有历史消息（按时间排序，模拟内存列表的顺序）
        List<ConversationMemory> memories = conversationMemoryDAO
                .findByConversationId(conversationId);

        // 2. 处理空列表情况
        if (CollectionUtils.isEmpty(memories)) {
            return List.of();
        }
        return memories.stream()
                .skip(Math.max(0, memories.size() - lastN))
                .map(this::getMessage)
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        conversationMemoryDAO.deleteMessage(conversationId);
    }


    private Message getMessage(ConversationMemory conversationMemory) {
        String memory = conversationMemory.getMemory();
        Gson gson = new Gson();

        return (Message) gson.fromJson(memory, MessageTypeEnum.fromValue(conversationMemory.getType()).getClazz());
    }
}
