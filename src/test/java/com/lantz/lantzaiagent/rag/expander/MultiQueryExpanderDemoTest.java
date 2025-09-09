package com.lantz.lantzaiagent.rag.expander;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/25
 *
 * @author Lantz
 * @version 1.0
 * @Description MultiQueryExpanderDemoTest
 * @since 1.8
 */
@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpanderDemo;

    @Test
    void queryExpander() {
        List<Query> queries = multiQueryExpanderDemo.queryExpander("啥是延边刺客？他会啥？");
        Assertions.assertNotNull(queries);
    }
}