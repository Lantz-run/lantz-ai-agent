package com.lantz.lantzaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.lantz.lantzaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/1
 *
 * @author Lantz
 * @version 1.0
 * @Description FileOperationTool  文件操作
 * @since 1.8
 */

public class FileOperationTool {
    /**
     * 文件保存路径
     */
    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    /**
     * 读取文件
     * @param fileName
     * @return
     */
    @Tool(description = "Read content from a file")
    public String FileRead(@ToolParam(description = "Name of a file to read") String fileName){
        String filePath = FILE_DIR + "/" + fileName;

        try {
            return FileUtil.readUtf8String(filePath);
        } catch (IORuntimeException e) {
            return "Error reading file" + e.getMessage();
        }
    }

    /**
     * 写文件（路径 + 内容）
     * @param fileName
     * @return
     */
    @Tool(description = "Write content from a file")
    public String FileWrite(
            @ToolParam(description = "Name of a file to write") String fileName,
            @ToolParam(description = "Content need to write in a file") String content
            ){
        String filePath = FILE_DIR + "/" + fileName;

        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File is written successfully to" + filePath;
        } catch (IORuntimeException e) {
            return "File is written failed to" + e.getMessage();
        }
    }
}
