package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.dto.MailDto;
import cn.mlm.mall.kill.mapper.ItemKillMapper;
import cn.mlm.mall.kill.mapper.ItemKillSuccessMapper;
import cn.mlm.mall.kill.pojo.ItemKillSuccess;
import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * RabbitMQ接收消息服务
 */
@Service
public class RabbitReceiverService {


    public static final Logger log = LoggerFactory.getLogger(RabbitReceiverService.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;
    @Autowired
    private ItemKillMapper itemKillMapper;


    /**
     * 监听RabbitMQ消息
     * 秒杀异步邮件通知-接收消息
     *
     * @param info
     */
    @RabbitListener(queues = {"${mq.kill.item.success.email.queue}"}, containerFactory = "singleListenerContainer")
    public void consumeEmailMsg(KillSuccessUserInfo info) {
        try {
            log.info("秒杀异步邮件通知-接收消息:{}", info);
            // TODO: 2019/12/6 真正发送邮件
            final String content = String.format(env.getProperty("mail.kill.item.success.content"), info.getItemName(), info.getCode());
            MailDto mailDto = new MailDto(env.getProperty("mail.kill.item.success.subject"), content, new String[]{info.getEmail()});
            mailService.sendHTMLMail(mailDto);
        } catch (Exception e) {
            log.error("秒杀异步邮件通知-接收消息-发生异常：", e.fillInStackTrace());
        }
    }

    /**
     * 用户秒杀成功后超时未支付-监听者
     * 监听消息队列消息，执行消费者模式
     *
     * @param info
     */
    @RabbitListener(queues = {"${mq.kill.item.success.kill.dead.real.queue}"}, containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(KillSuccessUserInfo info) {
        try {
            log.info("用户秒杀成功后超时未支付-监听者-接收消息:{}", info);
            if (info != null) {
                //根据秒杀订单ID查询秒杀订单状态
                ItemKillSuccess itemKillSuccess = itemKillSuccessMapper.queryById(info.getCode());
                if (itemKillSuccess != null && itemKillSuccess.getStatus().intValue() == 0) {
                    //取消超时订单
                    itemKillSuccessMapper.expireOrder(info.getCode());
                    //将取消的订单恢复库存
                    itemKillMapper.restoreItemInventory(info.getItemId());
                }
            }
        } catch (Exception e) {
            log.error("用户秒杀成功后超时未支付-监听者-发生异常：", e.fillInStackTrace());
        }
    }
}
