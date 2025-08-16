package com.lantz.lantzaiagent.demo.advisor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SensitiveWordDetector {

    // 核心违禁词库（实际项目应从数据库/文件加载）
    private static final Set<String> BANNED_WORDS = new HashSet<>(Arrays.asList(
        "自杀", "自残", "暴力", "色情", "毒品", "诈骗", "迷药", "窃听", 
        "包养", "报复", "裸照", "人肉", "上床", "卖淫", "嫖娼", "乱伦",
        "殴打", "下药", "囚禁", "驯服", "必须听话", "破处", "性剥削"
    ));

    private static final String replaceText = "检测到伤害性意图，亲密关系中的冲突应通过沟通或法律途径解决。需要心理支持请拨打12355青少年服务台";

    /**
     * 检测字符串是否包含违禁词
     * @param content 待检测文本
     * @return 包含违禁词返回true，否则false
     */
    public static boolean containsBannedWord(String content) {
        if (content == null || content.isEmpty()) return false;
        
        // 统一转为小写消除大小写差异
        String normalized = content.toLowerCase();
        
        // 遍历违禁词库进行检测
        for (String word : BANNED_WORDS) {
            if (normalized.contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 增强版检测（带匹配词返回）
     * @param content 待检测文本
     * @return 匹配到的违禁词，若无则返回空字符串
     */
    public static String detectBannedWord(String content) {
        if (content == null) return "";
        
        String normalized = content.toLowerCase();
        for (String word : BANNED_WORDS) {
            String lowerWord = word.toLowerCase();
            if (normalized.contains(lowerWord)) {
                return replaceText;  // 返回原始大小写的违禁词
            }
        }
        return "";
    }

    // 使用示例
    public static void main(String[] args) {
        String testText = "警惕网络诈骗和毒品陷阱";
        
        System.out.println("基础检测结果: " + containsBannedWord(testText));
        System.out.println("详细检测结果: " + detectBannedWord(testText));
    }
}