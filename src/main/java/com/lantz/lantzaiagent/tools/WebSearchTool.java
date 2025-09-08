package com.lantz.lantzaiagent.tools;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/1
 *
 * @author Lantz
 * @version 1.0
 * @Description WebSearchTool
 * @since 1.8
 */

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 联网搜索工具
 */
public class WebSearchTool {

    private final String URL = "https://www.searchapi.io/api/v1/search";

    private String apiKey;

    public WebSearchTool(String apiKey) {
        this.apiKey = apiKey;
    }

    @Tool(description = "Search information from baidu search engine")
    public String searchWeb(
            @ToolParam(description = "this is search query keyword") String query){

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("q", query);
            map.put("api_key", apiKey);
            map.put("engine", "baidu");

            String response = HttpUtil.get(URL, map);

            // 转换为 json 对象
            JSONObject jsonObject = JSONUtil.parseObj(response);
            // 只要 organic_results
            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
            List<Object> objects = organicResults.subList(0, 5); // 只要前五个子数组的数据

            String result = objects.stream().map(obj -> {
                JSONObject tmpJsonObject = (JSONObject) obj;
                return tmpJsonObject.toString();
            }).collect(Collectors.joining(", "));
            return result;
        } catch (Exception e) {
            return "Error search information from baidu" + e.getMessage();
        }

    }

}




















