package com.lantz.lantzaiagent.rag;

import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/17
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveApp
 * @since 1.8
 */

/**
 * 读取 Markdown文档
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 读取 Markdown 文档
     * @return
     */
    public List<Document> loadMarkdown(){
        List<Document> documentList = new ArrayList<>();
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:documents/*.md");
            // 从多个文件中读取文章
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                // 获取文档后 6 个字符作为标签
                String status = filename.substring(filename.length() - 6, filename.length() - 4);
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("status", status) // 添加状态标签
                        .build();

                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                documentList.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("无法读取 Markdown 文档");
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "读取 Markdown 文档失败");
        }
        return documentList;
    }

}
