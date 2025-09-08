package com.lantz.lantzaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import com.lantz.lantzaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/2
 *
 * @author Lantz
 * @version 1.0
 * @Description ResourceDownloadTool
 * @since 1.8
 */
public class ResourceDownloadTool {

    @Tool(description = "Download resource from a given url")
    public String downloadResource(
            @ToolParam(description = "URL of the resource to download") String url,
            @ToolParam(description = "Name of the file to save the download resource") String fileName){

        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(fileDir);
            // 使用hutool下载文件
            HttpUtil.downloadFile(url, new File(filePath));
            return "Download resource successfully to: " + filePath;
        } catch (Exception e) {
            return "Error downloading resource" + e.getMessage();
        }

    }

}
