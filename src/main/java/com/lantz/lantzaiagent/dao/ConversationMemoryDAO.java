package com.lantz.lantzaiagent.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lantz.lantzaiagent.model.entity.ConversationMemory;
import com.lantz.lantzaiagent.mapper.ConversationMemoryMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 少年的魂
* @description 针对表【conversation_memory】的数据库操作
* @createDate 2025-09-13 13:27:29
*/
@Component
public class ConversationMemoryDAO extends ServiceImpl<ConversationMemoryMapper, ConversationMemory> {

    public List<ConversationMemory> getMessage(String conversationId){
        return this.lambdaQuery()
                .eq(ConversationMemory::getConversationId, conversationId)
                .list();
    }

    public boolean deleteMessage(String conversationId){
        return this.lambdaUpdate()
                .eq(ConversationMemory::getConversationId, conversationId)
                .remove();
    }

    public List<ConversationMemory> findByConversationId(String conversationId) {
        // 使用LambdaQueryWrapper构建查询条件
        return this.lambdaQuery()
                .eq(ConversationMemory::getConversationId, conversationId)  // 匹配对话ID
                .orderByAsc(ConversationMemory::getCreatedTime)  // 按创建时间升序排列，保证消息顺序
                .list();  // 执行查询并返回结果列表
    }


}




