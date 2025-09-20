package com.lantz.lantzaiagent.advisor;

import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/14
 *
 * @author Lantz
 * @version 1.0
 * @Description ProhibitedAdvisor
 * @since 1.8
 */
@Slf4j
public class ProhibitedAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // 核心违禁词库（实际项目应从数据库/文件加载）
    private static final String BANNED_DIR = "bannedText/banned_words_1.txt";

    private Set<String> bannedSet;

    /**
     * 创建违禁词构造方法，
     */
    public ProhibitedAdvisor() {
        this.bannedSet = loadBannedWords(BANNED_DIR);
        log.info("初始化违禁词 Advisor，加载了 {} 个违禁词", bannedSet.size());
    }

    /**
     * 创建违禁词构造方法，
     */
    public ProhibitedAdvisor(String bannedFilenamePath) {
        this.bannedSet = loadBannedWords(BANNED_DIR);
        log.info("初始化违禁词拦截器，加载了 {} 个违禁词", bannedSet.size());
    }

    @Override
    public int getOrder() {
        return 5; // 违禁词advisor要比其他 advisor 先执行
    }

    @Override
    public String getName() {
        return "违禁词拦截器";
    }

    private AdvisedRequest before(AdvisedRequest request) {
        log.info("AI request: {}", request.userText());
        return request;
    }

    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("AI response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);

        Flux<AdvisedResponse> advisedResponseFlux = chain.nextAroundStream(checkRequest(advisedRequest));

        return advisedResponseFlux;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        // 1. 处理请求（前置处理）
        advisedRequest = this.before(advisedRequest);

        // 2. 调用链中下一个 Advisor
        AdvisedResponse advisedResponse = chain.nextAroundCall(checkRequest(advisedRequest));

        // 3. 处理响应（后置处理）
        this.observeAfter(advisedResponse);

        return advisedResponse;
    }


    /**
     * 检查请求中是否有违禁词
     * @param request
     * @return
     */
    private AdvisedRequest checkRequest(AdvisedRequest request) {
        String userText = request.userText();
        if (isContainBanWord(userText)) {
            log.error("检测到用户输入文本存在违禁词");
            throw new BusinessException(ErrorCode.TEXT_ERROR, "用户输入存在违禁词");
        }
        return request;
    }

    /**
     * 检查文本是否有违禁词
     * @param text
     * @return
     */
    private boolean isContainBanWord(String text) {
        if (!StringUtils.hasText(text)) return false;
        for (String word: bannedSet) {
            if (text.contains(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从文件加载违禁词列表
     * @param filename 文件名
     */
    public Set<String> loadBannedWords(String filename){
        try {
            Path path = Paths.get(filename);

            // 读取文件所有行并处理
            Set<String> bannedSet = Files.lines(path)
                    .map(String::trim) // 去除首尾空格
                    .filter(line -> !line.isEmpty()) // 跳过空行
                    .filter(line -> !line.startsWith("#")) // 跳过注释行
                    .collect(Collectors.toSet());
            log.info("从文件{}中加载{}个违禁词", path.getFileName(), bannedSet.size());
            return bannedSet;
        } catch (IOException e) {
            log.error("从文件中加载违禁词失败");
            return new HashSet<>();
        }
    }

}
