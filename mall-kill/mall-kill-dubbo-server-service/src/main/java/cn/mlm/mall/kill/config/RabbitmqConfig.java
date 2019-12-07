package cn.mlm.mall.kill.config;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * RabbitMQ配置
 *
 * @author linSir
 */
@Configuration
public class RabbitmqConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 多个消费者
     *
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory, connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式-NONE
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency", int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency", int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch", int.class));
        return factory;
    }

    /**
     * 单一消费者
     *
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory() {
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        //设置缓存工厂
        containerFactory.setConnectionFactory(connectionFactory);
        //消息序传输格式
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        //设置并发消费者
        containerFactory.setConcurrentConsumers(1);
        //设置最大并发消费者
        containerFactory.setMaxConcurrentConsumers(1);
        //设置预取计数
        containerFactory.setPrefetchCount(1);
        containerFactory.setTxSize(1);
        return containerFactory;
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        //发布者确认
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                logger.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, b, s);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.warn("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message);
            }
        });
        return rabbitTemplate;
    }

    /**
     * //构建异步发送邮箱通知的消息模型
     * <p>
     * <p>
     * 创建消息队列
     *
     * @return
     */
    @Bean
    public Queue successEmailQueue() {
        return new Queue(env.getProperty("mq.kill.item.success.email.queue"), true);
    }

    /**
     * 消息持久化，不自动删除
     * 交换机
     *
     * @return
     */
    @Bean
    public TopicExchange successEmailExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.email.exchange"), true, false);
    }


    /**
     * 消息队列绑定的交换机
     *
     * @return
     */
    @Bean
    public Binding successEmailBinding() {
        return BindingBuilder.bind(successEmailQueue()).to(successEmailExchange()).with(env.getProperty("mq.kill.item.success.email.routing.key"));
    }

    /**
     * 构建秒杀成功之后-订单超时未支付的死信队列消息模型
     * <p>
     * 1.创建死信队列
     *
     * @return
     */
    @Bean
    public Queue successKillDeadQueue() {
        Map<String, Object> argsMap = Maps.newHashMap();
        argsMap.put("x-dead-letter-exchange", env.getProperty("mq.kill.item.success.kill.dead.exchange"));
        argsMap.put("x-dead-letter-routing-key", env.getProperty("mq.kill.item.success.kill.dead.routing.key"));
        return new Queue(env.getProperty("mq.kill.item.success.kill.dead.queue"), true, false, false, argsMap);
    }

    /**
     * 基本交换机
     * @return
     */
    @Bean
    public TopicExchange successKillDeadProdExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"), true, false);
    }

    /**
     * 创建基本交换机+基本路由 -> 死信队列 的绑定
     */
    @Bean
    public Binding successKillDeadProdBinding() {
        return BindingBuilder.bind(successKillDeadQueue()).to(successKillDeadProdExchange()).with(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
    }

    /**
     * 真正的队列
     *
     * @return
     */
    @Bean
    public Queue successKillRealQueue() {
        return new Queue(env.getProperty("mq.kill.item.success.kill.dead.real.queue"), true);
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean
    public TopicExchange successKillDeadExchange() {
        return new TopicExchange(env.getProperty("mq.kill.item.success.kill.dead.exchange"), true, false);
    }

    /**
     * 死信交换机+死信路由->真正队列 的绑定
     *
     * @return
     */
    @Bean
    public Binding successKillDeadBinding() {
        return BindingBuilder.bind(successKillRealQueue()).to(successKillDeadExchange()).with(env.getProperty("mq.kill.item.success.kill.dead.routing.key"));
    }

}
