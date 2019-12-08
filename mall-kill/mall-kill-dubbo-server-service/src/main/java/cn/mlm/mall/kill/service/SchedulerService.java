package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.mapper.ItemKillMapper;
import cn.mlm.mall.kill.mapper.ItemKillSuccessMapper;
import cn.mlm.mall.kill.pojo.ItemKillSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务：定时取消超时订单并恢复库存
 *
 * @author linSir
 */
@Service
public class SchedulerService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;
    @Autowired
    private ItemKillMapper itemKillMapper;
    @Autowired
    private Environment env;

    /**
     * 定时获取status=0的订单并判断是否超过TTL，然后进行失效
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void schedulerExpireOrders() {
        logger.info("定时获取status=0的订单并判断是否超过TTL时间执行");
        try {
            List<ItemKillSuccess> list = itemKillSuccessMapper.selectExpireOrders();
            if (list != null && !list.isEmpty()) {
                list.stream().forEach(itemKillSuccess -> {
                    if (itemKillSuccess != null && itemKillSuccess.getDiffTime() > env.getProperty("scheduler.expire.orders.time", Integer.class)) {
                        //失效已经秒杀成功订单
                        itemKillSuccessMapper.expireOrder(itemKillSuccess.getCode());
                        //并恢复秒杀商品的库存
                        itemKillMapper.restoreItemInventory(itemKillSuccess.getItemId());
                    }
                });
            }
        } catch (Exception e) {
            logger.info("定时获取status=0的订单并判断是否超过TTL时间-出现异常：", e.fillInStackTrace());
        }
    }

   /* @Scheduled(cron = "0/20 * * * * ?")
    public void scheduler2() {
        logger.info("定时任务----2");
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void scheduler3() {
        logger.info("定时任务----3");
    }*/

}
