package cn.mlm.mall.kill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送邮件参数封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    //邮件主题
    private String subject;
    //邮件内容
    private String content;
    //接收人
    private String[] tos;
}
