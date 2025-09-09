package com.lantz.lantzimagesearchmcp.tools;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>Project: lantz-image-search-mcp
 * <p>Powered by Lantz On 2025/9/8
 *
 * @author Lantz
 * @version 1.0
 * @Description ImageSearchToolTest
 * @since 1.8
 */
@SpringBootTest
public class ImageSearchToolTest {

    @Resource
    private ImageSearchTool imageSearchTool;

    @Test
    public void imageSearch(){
        String query = "computer";
        String result = imageSearchTool.searchImage(query);
        Assertions.assertNotNull(result);
    }

}
