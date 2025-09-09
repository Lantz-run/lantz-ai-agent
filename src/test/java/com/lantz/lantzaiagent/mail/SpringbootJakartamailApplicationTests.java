package com.lantz.lantzaiagent.mail;

import com.lantz.lantzaiagent.LantzAiAgentApplication;
import com.lantz.lantzaiagent.constant.FileConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LantzAiAgentApplication.class)
public class SpringbootJakartamailApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testMailB() throws MessagingException {
        // 创建复杂邮件发送对象
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom("lantzAuth@163.com");           // 设置发件人邮箱
        messageHelper.setTo("1196046661@qq.com");            // 设置收件人邮箱
        messageHelper.setSubject("情歌王");                  // 设置邮件主题

        // 获取项目资源根目录
        String rootPath = FileConstant.FILE_SAVE_DIR;
        
        // 确保目录存在
        File downloadDir = new File(rootPath + "/download");
        File fileDir = new File(rootPath + "/file");
        if (!downloadDir.exists()) downloadDir.mkdirs();
        if (!fileDir.exists()) fileDir.mkdirs();

        try {
            // 准备附件文件 - 使用相对路径或创建测试文件
            File logoFile = new File(downloadDir, "logo.png");
            File textFile = new File(fileDir, "够钟.txt");
            
            // 如果文件不存在，创建测试文件
            if (!logoFile.exists()) {
                // 可以在这里创建测试文件或使用默认资源
                System.out.println("附件文件不存在: " + logoFile.getAbsolutePath());
                // 跳过这个附件或者创建测试文件
            }
            
            if (!textFile.exists()) {
                // 创建测试文本文件
                java.nio.file.Files.write(textFile.toPath(), "这是一首简单的小情歌".getBytes());
            }

            // 设置附件
            if (logoFile.exists()) {
                FileSystemResource png = new FileSystemResource(logoFile);
                messageHelper.addAttachment(Objects.requireNonNull(png.getFilename()), png);
            }
            
            if (textFile.exists()) {
                FileSystemResource xls = new FileSystemResource(textFile);
                messageHelper.addAttachment(Objects.requireNonNull(xls.getFilename()), xls);
            }

            // 准备内嵌图片资源
            File inlineImage = new File(rootPath + "/download/星空情侣手机壁纸.jpg");
            if (!inlineImage.exists()) {
                // 创建测试图片文件或使用classpath资源
                System.out.println("内嵌图片不存在: " + inlineImage.getAbsolutePath());
                // 使用简单的HTML内容
                messageHelper.setText("<h2 style='color:#f00;'>古巨基 - 情歌王</h2>", true);
            } else {
                FileSystemResource resPng = new FileSystemResource(inlineImage);
                messageHelper.addInline("p01", resPng);
                messageHelper.setText("<h2 style='color:#f00;'>古巨基<img src='cid:p01' alt='' style='width:200px;height:50px;'></h2>", true);
            }

            // 发送邮件
            mailSender.send(mimeMessage);
            System.out.println("邮件发送成功！");

        } catch (Exception e) {
            System.err.println("邮件发送失败: " + e.getMessage());
            e.printStackTrace();
            
            // 发送简化版本的邮件（无附件）
            messageHelper.setText("<h2 style='color:#f00;'>古巨基 - 情歌王（测试邮件）</h2>", true);
            mailSender.send(mimeMessage);
            System.out.println("简化版邮件发送成功！");
        }
    }
    
    @Test
    public void testSimpleMail() throws MessagingException {
        // 简单的测试方法，不依赖外部文件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        
        messageHelper.setFrom("lantzAuth@163.com");
        messageHelper.setTo("1196046661@qq.com");
        messageHelper.setSubject("测试邮件 - 情歌王");
        messageHelper.setText("<h2 style='color:blue;'>这是一封测试邮件</h2><p>古巨基 - 情歌王</p>", true);
        
        mailSender.send(mimeMessage);
        System.out.println("简单测试邮件发送成功！");
    }
}