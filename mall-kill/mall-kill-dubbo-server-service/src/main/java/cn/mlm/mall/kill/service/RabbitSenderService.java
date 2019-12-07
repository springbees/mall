package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.mapper.ItemKillSuccessMapper;
import cn.mlm.mall.kill.pojo.ItemKillSuccess;
import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author linSir
 * 服务化开发 RabbitMQ发送邮件服务
 */
@Service
public class RabbitSenderService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String TAG = "RabbitSenderService";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    /**
     * 秒杀成功异步发送邮件通知消息
     *
     * @param orderNo
     */
    public void sendKillSuccessEmailMsg(String orderNo) {
        logger.info("秒杀成功异步发送邮件通知消息-准备发送消息:{}", orderNo);
        try {
            if (StringUtils.isNotBlank(orderNo)) {
                //根据订单编号查询秒杀订单是否存在
                KillSuccessUserInfo itemKillSuccess = itemKillSuccessMapper.selectId(orderNo);
                if (itemKillSuccess != null) {
                    // TODO: 2019/12/6 发送消息逻辑
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    //发送消息的交换机
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
                    //发送消息的路由
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));
                    // TODO: 2019/12/6 将订单号查询的对象充当消息发送
                    rabbitTemplate.convertAndSend(itemKillSuccess, message -> {
                        MessageProperties messageProperties = message.getMessageProperties();
                        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                        messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
                        return message;
                    });
                }
            }
        } catch (Exception e) {
            logger.error("秒杀成功异步发送邮件通知消息-发生异常，消息为：{}", orderNo, e.fillInStackTrace());
        }
    }

    /**
     * 秒杀成功后生成抢购订单-发送信息入死信队列，等待着一定时间失效超时未支付的订单
     *
     * @param orderNo
     */
    public void sendKillSuccessOrderExpireMsg(final  String orderNo) {
        try {
            if (StringUtils.isNotBlank(orderNo)) {
                //根据秒杀商品的ID查询该秒杀提交的订单
                KillSuccessUserInfo info = itemKillSuccessMapper.selectId(orderNo);
                if (info != null) {
                    logger.info("秒杀成功后生成抢购订单生成对象信息：-> {}", info);
                    // TODO: 2019/12/6 死信队列实现逻辑
                    //消息传输格式
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    //设置交换机
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"));
                    //设置路由
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
                    //发送消息
                    rabbitTemplate.convertAndSend(info,new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            //构建消息属性
                            MessageProperties properties = message.getMessageProperties();
                            //设置消息持久性
                            properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            //消息头
                            properties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
                            //TODO：动态设置TTL(为了测试方便，暂且设置10s)
                            properties.setExpiration(env.getProperty("mq.kill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("秒杀成功后生成抢购订单-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}", orderNo, e.fillInStackTrace());
        }

    }
}
