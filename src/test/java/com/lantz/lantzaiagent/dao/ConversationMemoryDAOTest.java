package com.lantz.lantzaiagent.dao;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/15
 *
 * @author Lantz
 * @version 1.0
 * @Description ConversationMemoryDAOTest
 * @since 1.8
 */
@SpringBootTest
class ConversationMemoryDAOTest {

    @Resource
    private ConversationMemoryDAO conversationMemoryDAO;

    @Test
    void isConversationIdExist1() {
        String chatId = "a28a13e6-8b1a-4f47-8ac4-2b9a6db97808";
        boolean conversationIdExist = conversationMemoryDAO.isConversationIdExist(chatId);
        Assertions.assertNotNull(conversationIdExist);
    }
}