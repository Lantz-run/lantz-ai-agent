package com.lantz.lantzaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/10
 *
 * @author Lantz
 * @version 1.0
 * @Description LantzManusTest
 * @since 1.8
 */
@SpringBootTest
class LantzManusTest {
    @Resource
    private LantzManus lantzManus;

    @Test
    void run(){
        String userPrompt = """  
                我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点，  
                并结合一些网络图片，制定一份详细的约会计划，  
                并以 PDF 格式输出, 邮箱地址是1196046661@qq.com""";
        String ans = lantzManus.run(userPrompt);
        Assertions.assertNotNull(ans);

    }

}