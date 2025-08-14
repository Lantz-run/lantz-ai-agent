package com.lantz.lantzaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONArray;

public class HttpInvokeAi {
    
    private static final String API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    private static final String DASHSCOPE_API_KEY = "your_api_key_here"; // 替换为实际的API Key
    
    public static void main(String[] args) {
        // 构建请求体
        JSONObject requestBody = buildRequestBody();
        
        try {
            // 发送HTTP POST请求
            HttpResponse response = HttpRequest.post(API_URL)
                    .header("Authorization", "Bearer " + DASHSCOPE_API_KEY)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .execute();
            
            // 输出响应结果
            System.out.println("状态码: " + response.getStatus());
            System.out.println("响应体: " + response.body());
            
        } catch (Exception e) {
            System.err.println("请求发生异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 构建请求体JSON
     */
    private static JSONObject buildRequestBody() {
        // 构建消息数组
        JSONArray messages = new JSONArray();
        
        // 系统消息
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");
        messages.add(systemMessage);
        
        // 用户消息
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "你是谁？");
        messages.add(userMessage);
        
        // 构建input对象
        JSONObject input = new JSONObject();
        input.put("messages", messages);
        
        // 构建parameters对象
        JSONObject parameters = new JSONObject();
        parameters.put("result_format", "message");
        
        // 构建完整的请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "qwen-plus");
        requestBody.put("input", input);
        requestBody.put("parameters", parameters);
        
        return requestBody;
    }
}