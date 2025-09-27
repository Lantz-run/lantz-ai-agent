package com.lantz.lantzaiagent.dao;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lantz.lantzaiagent.constant.CommonConstant;
import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import com.lantz.lantzaiagent.model.dto.ai.ConversationMemoryQueryRequest;
import com.lantz.lantzaiagent.model.dto.user.UserQueryRequest;
import com.lantz.lantzaiagent.model.entity.ConversationMemory;
import com.lantz.lantzaiagent.mapper.ConversationMemoryMapper;
import com.lantz.lantzaiagent.model.entity.User;
import com.lantz.lantzaiagent.model.vo.UserVO;
import com.lantz.lantzaiagent.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean isConversationIdExist(String conversationId) {
        return this.lambdaQuery()
                .eq(ConversationMemory::getConversationId, conversationId)
                .count() > 0;
    }

    /**
     * 查询会话表
     * @param conversationMemoryQueryRequest
     * @return
     */
    public QueryWrapper<ConversationMemory> getQueryWrapper(ConversationMemoryQueryRequest conversationMemoryQueryRequest) {
        if (conversationMemoryQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = conversationMemoryQueryRequest.getId();
        String conversationId = conversationMemoryQueryRequest.getConversationId();
        String memory = conversationMemoryQueryRequest.getMemory();
        String sortField = conversationMemoryQueryRequest.getSortField();
        String sortOrder = conversationMemoryQueryRequest.getSortOrder();

        QueryWrapper<ConversationMemory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(conversationId), "conversationId", conversationId);
        queryWrapper.like(StringUtils.isNotBlank(memory), "memory", memory);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    public ConversationMemory getConversationMemory(ConversationMemory conversationMemory) {
        if (conversationMemory == null) {
            return null;
        }
        ConversationMemory conversationMemory1 = new ConversationMemory();
        BeanUtils.copyProperties(conversationMemory, conversationMemory1);
        return conversationMemory1;
    }

    public List<ConversationMemory> getConversationMemory(List<ConversationMemory> conversationMemoryList) {
        if (CollUtil.isEmpty(conversationMemoryList)) {
            return new ArrayList<>();
        }
        return conversationMemoryList.stream().map(this::getConversationMemory).collect(Collectors.toList());
    }
}




