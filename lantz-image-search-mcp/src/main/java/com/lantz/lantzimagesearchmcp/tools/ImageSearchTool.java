package com.lantz.lantzimagesearchmcp.tools;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>Project: lantz-image-search-mcp
 * <p>Powered by Lantz On 2025/9/8
 *
 * @author Lantz
 * @version 1.0
 * @Description ImageSearchTool
 * @since 1.8
 */
@Service
public class ImageSearchTool {

    // https://www.pexels.com/ 上的密钥
    private static final String PEXELS_API_KEY = "YOUR_KEY";

    // pexels 搜索 url
    private static final String SEARCH_URL = "https://api.pexels.com/v1/search";


    @Tool(description = "Search image from web")
    public String searchImage(@ToolParam(description = "Search image keywords") String query) {
        try {
            return String.join(",", searchMediumImage(query));
        } catch (Exception e) {
            return "Error search image, " + e.getMessage();
        }
    }

    /**
     * 搜索中等尺寸的图片
     * @param query
     * @return
     */
    public List<String> searchMediumImage(String query){
        // 设置请求头（包含密钥）
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", PEXELS_API_KEY);

        // 设置请求参数（包含query，可以根据实际情况设置 page，per_page参数）
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        // 发送 GET 请求
        String response = HttpUtil.createGet(SEARCH_URL)
                .addHeaders(header)
                .form(params)
                .execute()
                .body();

        // 响应 JSON 请求
        return JSONUtil.parseObj(response)
                .getJSONArray("photos")
                .stream()
                .map(photosObj -> (JSONObject) photosObj)
                .map(photoObj -> photoObj.getJSONObject("src"))
                .map(photo -> photo.getStr("medium"))
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());

    }

}
