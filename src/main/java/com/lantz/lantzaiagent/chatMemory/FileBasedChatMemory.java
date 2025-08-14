package com.lantz.lantzaiagent.chatMemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/13
 *
 * @author Lantz
 * @version 1.0
 * @Description FileBasedChatMemory
 * @since 1.8
 */

/**
 * 基于文件实现 AI 对话持久化
 */
public class FileBasedChatMemory implements ChatMemory {

    // 定义文件路径
    private final String BASE_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    /**
     * 构造对象时，指定文件保存目录
     *
     * @param dir
     */
    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists()) {
            baseDir.mkdir();
        }
    }

    /**
     * 添加对话记录
     *
     * @param conversationId
     * @param messages
     */
    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessage = getOrCreateConversation(conversationId);
        conversationMessage.addAll(messages);
        saveConversation(conversationId, conversationMessage);
    }

    /**
     * 获取对话消息
     *
     * @param conversationId
     * @param lastN
     * @return
     */
    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> conversation = getOrCreateConversation(conversationId);
        return conversation.stream().skip(Math.max(0, conversation.size() - lastN)).toList();
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取或者创建对话
     *
     * @param conversationId
     * @return
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messageList = new ArrayList<>();
        // 如果对话已经存在
        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messageList = kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return messageList;
    }

    /**
     * 保存对话到文件
     *
     * @param conversationId
     * @param messages
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成对话文件
     *
     * @param conversationId
     * @return
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}
