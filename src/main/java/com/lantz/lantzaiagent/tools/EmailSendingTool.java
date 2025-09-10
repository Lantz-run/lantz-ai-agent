package com.lantz.lantzaiagent.tools;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/6
 *
 * @author Lantz
 * @version 1.0
 * @Description EmailSendingTool
 * @since 1.8
 */

import cn.hutool.core.io.FileUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.lantz.lantzaiagent.constant.FileConstant;
import com.lantz.lantzaiagent.exception.BusinessException;
import com.lantz.lantzaiagent.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.Properties;

/**
 * 发送邮件工具
 */
public class EmailSendingTool {

    private JavaMailSender javaMailSender;

    private final String fromEmail = "YOUR_FROMEMAIL"; // 推荐163邮箱

    private String ATTATCH_FILE_DIR = FileConstant.FILE_SAVE_DIR + "/attachment";

    private String host;

    private int port;

    private String username;

    private String password;


    public EmailSendingTool(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * 初始化邮件发送者
     */
    private void initEmailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        //初始化默认参数
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");
        javaMailSender = mailSender;
    }

    /**
     * 发送纯文本格式邮件
     *
     * @param subject
     * @param toEmail
     * @param textContent
     * @return
     */
    @Tool(description = "Send a text email")
    public String sendTextEmail(
            @ToolParam(description = "the subject of the email") String subject,
            @ToolParam(description = "Recipient email address") String toEmail,
            @ToolParam(description = "text content of the email") String textContent) {

        try {
            initEmailSender();
            // 创建复杂邮件发送对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(fromEmail);        // 设置发件人邮箱
            messageHelper.setTo(toEmail);            // 设置收件人邮箱
            messageHelper.setSubject(subject);       // 设置邮件主题

            // 使用简单的HTML内容
            messageHelper.setText(textContent, true);

            // 发送邮件
            javaMailSender.send(message);
            return "Send html email successfully to " + toEmail;
        } catch (MessagingException e) {
            return "Fail sending email! " + e.getMessage();
        }
    }

    /**
     * 发送 html 格式邮件
     *
     * @param subject
     * @param toEmail
     * @param htmlContent
     * @return
     */
    @Tool(description = "Send a html email")
    public String sendHtmlEmail(
            @ToolParam(description = "the subject of the email") String subject,
            @ToolParam(description = "Recipient email address") String toEmail,
            @ToolParam(description = "html content of the email") String htmlContent) {

        try {
            initEmailSender();
            // 创建复杂邮件发送对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(fromEmail);        // 设置发件人邮箱
            messageHelper.setTo(toEmail);            // 设置收件人邮箱
            messageHelper.setSubject(subject);       // 设置邮件主题

            // 使用简单的HTML内容
            messageHelper.setText(htmlContent, true);

            // 发送邮件
            javaMailSender.send(message);
            return "Send html email successfully to " + toEmail;
        } catch (MessagingException e) {
            return "Fail sending email! " + e.getMessage();
        }
    }

    /**
     * 发送携带附件格式邮件
     *
     * @param subject
     * @param toEmail
     * @param htmlContent
     * @return
     */
    @Tool(description = "Send a attachment email")
    public String sendAttachEmail(
            @ToolParam(description = "the subject of the email") String subject,
            @ToolParam(description = "Recipient email address") String toEmail,
            @ToolParam(description = "html content of the email") String htmlContent,
            @ToolParam(description = "The filename of attachment") String attachmentFileName) {

        try {
            initEmailSender();
            // 创建复杂邮件发送对象
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(fromEmail);        // 设置发件人邮箱
            messageHelper.setTo(toEmail);            // 设置收件人邮箱
            messageHelper.setSubject(subject);       // 设置邮件主题
            String fileSaveDir = FileConstant.FILE_SAVE_DIR + "/attachment";

            // 创建目录
            FileUtil.mkdir(ATTATCH_FILE_DIR);

            File filePath = new File(fileSaveDir, attachmentFileName);

            messageHelper.addAttachment(attachmentFileName, filePath);
            // 使用简单的HTML内容
            messageHelper.setText(htmlContent, true);

            // 发送邮件
            javaMailSender.send(message);
            return "Send html email successfully to " + toEmail;
        } catch (MessagingException e) {
            return "Fail sending email! " + e.getMessage();
        }
    }

    /**
     * 生成文件
     *
     * @param attachmentSuffix
     * @return
     */
    // todo 1. 生成的文件并作为附件发送邮件; 2. 支持联网生成内容写入文件发送
    private String generateFile(String fileName, String content, String attachmentSuffix) {
        if (attachmentSuffix.equals(".pdf")) {
            String fileDir = FileConstant.FILE_SAVE_DIR + "/attachment/pdf";
            String filePath = fileDir + "/" + fileName;
            try {
                // 创建目录
                FileUtil.mkdir(filePath);
                // 创建 pdfWriter 和 pdfDocument 对象
                try (PdfWriter writer = new PdfWriter(filePath);
                     PdfDocument pdf = new PdfDocument(writer);
                     Document document = new Document(pdf)) {

                    // 使用内置中文字体
                    PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
                    document.setFont(font);

                    // 创建段落
                    Paragraph paragraph = new Paragraph(content);

                    // 添加段落到文档
                    document.add(paragraph);
                }
                return "PDF generate successfully to: " + filePath;
            } catch (Exception e) {
                return "Error generate pdf" + e.getMessage();
            }
        }
        return "";
    }

}















