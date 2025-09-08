package com.lantz.lantzaiagent.mail;

import com.lantz.lantzaiagent.LantzAiAgentApplication;
import com.lantz.lantzaiagent.constant.FileConstant;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.util.Objects;

@SpringBootTest(classes = LantzAiAgentApplication.class)
public class SpringbootJakartamailApplicationTest {

    @Autowired
    private JavaMailSender mailSender;

    @Test
    public void testMailB() throws MessagingException {
        //创建复杂有限发送对象
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom("lantzAuth@163.com");           // 设置发件人邮箱（若配置默认邮箱则不用再设置）
        messageHelper.setTo("1196046661@qq.com");            // 设置收件人邮箱
//        messageHelper.setCc("xiaofeng500@qq.com");            // 设置抄报人邮箱（可以不填写）
//        messageHelper.setBcc("575814158@qq.com");             // 设置密送人邮箱（可以不填写）
        messageHelper.setSubject("情歌王");                  // 设置邮件主题

        //获取项目资源根目录 resources/file  并准备资源
        String rootPath = FileConstant.FILE_SAVE_DIR;
        FileSystemResource png = new FileSystemResource(new File(rootPath + "/download/logo.png"));
        FileSystemResource xls = new FileSystemResource(new File(rootPath + "/file/够钟.txt"));
//        FileSystemResource mp3 = new FileSystemResource(new File(rootPath + "/mu.mp3"));
//        FileSystemResource zip = new FileSystemResource(new File(rootPath + "/redis.zip"));

        //关于附件  资源  HTML 文本的设置
        //设置附件
        //设置一个 图片附件
        messageHelper.addAttachment(Objects.requireNonNull(png.getFilename()), png);
        //设置一个 excel附件
        messageHelper.addAttachment(Objects.requireNonNull(xls.getFilename()), xls);
        //设置一个 mp3附件
//        messageHelper.addAttachment(Objects.requireNonNull(mp3.getFilename()), mp3);
//        //设置一个 zip附件  不过发送垃圾附件可能会被识别 554 HL:IHU 发信IP因发送垃圾邮件或存在异常的连接行为
//        messageHelper.addAttachment(Objects.requireNonNull(zip.getFilename()), zip);

        //设置邮件内容   cid:资源id     在内容中引用资源    后面true代表是html内容
        messageHelper.setText("<h2 style='color:#f00;'>古巨基<img src='cid:p01' alt='' style='width:200px;height:50px;'></h2>", true);

        //设置资源
        FileSystemResource resPng = new FileSystemResource(new File(rootPath + "/b.png"));
        messageHelper.addInline("p01",resPng);

        //发送
        mailSender.send(mimeMessage);
    }
}