package com.lantz.lantzaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/2
 *
 * @author Lantz
 * @version 1.0
 * @Description PDFGenerationToolTest
 * @since 1.8
 */

@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generationPDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "demo1.pdf";
        String content = """
                # 深圳南山区约会计划\\n\\n以下是为您制定的约会计划，包含合适的约会地点和一些网络图片。\\n\\n## 约会地点推荐\\n\\n### 欢乐海岸·水幕光影秀\\n\\n- 亮点：免费露天灯光秀！曲面水幕+音乐喷泉，适合拥吻打卡。\\n- 晚餐推荐：陈记顺和牛肉丸粉（人均15元）、或选临水西餐厅享受烛光晚餐。\\n- 交通：9号线深圳湾公园站E口直达\\n\\n![欢乐海岸](date_location_1.jpg)\\n\\n### 南山公园\\n\\n- 亮点：山顶观景台，俯瞰深圳湾全景的绝佳位置。建议傍晚时分来，落日余晖，城市灯火，满分浪漫。\\n- 林间步道绵延5公里的天然氧吧。\\n\\n![南山公园](date_location_2.jpg)\\n\\n### 南头古城\\n\\n- 从海南粉店步行10分钟即可到达南头古城，这里有许多古建筑和小店，适合慢慢逛、拍照。\\n\\n![南头古城](date_location_3.jpg)
                """;
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }

}