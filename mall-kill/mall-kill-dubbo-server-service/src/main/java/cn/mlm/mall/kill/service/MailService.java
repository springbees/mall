package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.dto.MailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * 发送邮件服务
 */
@Service
@EnableAsync
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;


    /**
     * 发送带html元素修饰的邮件
     *
     * @param mailDto
     */
    @Async
    public void sendHTMLMail(final MailDto mailDto) {
        log.info("带HTML格式发送邮件: {}", mailDto);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setFrom(env.getProperty("mail.send.from"));
            //设置发送方
            mimeMessageHelper.setTo(mailDto.getTos());
            mimeMessageHelper.setSubject(mailDto.getSubject());
            //发送消息
            mimeMessageHelper.setText(mailDto.getContent(), true);
            mailSender.send(mimeMessage);
            log.info("发送邮件-发送成功!");
        } catch (Exception e) {
            log.info("发送邮件失败: {}", e.fillInStackTrace());
        }
    }
}
