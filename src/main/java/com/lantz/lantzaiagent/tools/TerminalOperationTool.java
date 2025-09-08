package com.lantz.lantzaiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/2
 *
 * @author Lantz
 * @version 1.0
 * @Description TreminalOperationTool
 * @since 1.8
 */
public class TerminalOperationTool {

    @Tool(description = "Execute a command in the terminal")
    public String executeCommand(
            @ToolParam(description = "the command to execute in the terminal") String command){
        StringBuilder output = new StringBuilder();

        try {
//            Process process = Runtime.getRuntime().exec(command);
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
            Process process = builder.start();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                output.append("Command execution failed with exit code: ").append(exitCode);
            }

        } catch (IOException | InterruptedException e) {
            output.append("Error executing the command: ").append(e.getMessage());
        }
        return output.toString();
    }

}
