package cn.mlm.mall.kill.service.impl;

import cn.mlm.mall.common.SysConstant;
import cn.mlm.mall.common.base.BaseServiceImpl;
import cn.mlm.mall.common.util.RandomUtil;
import cn.mlm.mall.common.util.SnowFlake;
import cn.mlm.mall.kill.mapper.ItemKillMapper;
import cn.mlm.mall.kill.mapper.ItemKillSuccessMapper;
import cn.mlm.mall.kill.pojo.ItemKill;
import cn.mlm.mall.kill.pojo.ItemKillSuccess;
import cn.mlm.mall.kill.service.IKillService;
import cn.mlm.mall.kill.service.RabbitSenderService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:35
 * 秒杀业务实现
 */
@Service
public class IKillServiceImpl extends BaseServiceImpl<ItemKill> implements IKillService {

    private static final Logger log = LoggerFactory.getLogger(IKillServiceImpl.class);
    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    @Autowired
    private RabbitSenderService rabbitSenderService;

    @Autowired
    private ItemKillMapper itemKillMapper;

    private SnowFlake snowFlake = new SnowFlake(2,3);

    /**
     * 商品秒杀核心业务逻辑的处理
     *
     * @param killId
     * @param userId
     * @return
     */
    @Override
    public Boolean killItem(Integer killId, Integer userId) {
        Boolean result = false;
        //TODO:判断当前用户是否已经抢购过当前商品
        if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
            //TODO:查询待秒杀商品详情
            ItemKill itemKill = itemKillMapper.getKillDetail(killId);
            //TODO:判断是否可以被秒杀canKill=1?
            if (itemKill != null && 1 == itemKill.getCanKill()) {
                //TODO:扣减库存-减一
                int res = itemKillMapper.updateKillItem(killId);
                if (res > 0) {
                    //TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
                    commonRecordKillSuccessInfo(itemKill, userId);
                    result = true;
                }
            }
        } else {
            throw new RuntimeException("您已经抢购过了该商品！");
        }
        return result;
    }

    /**
     * 通用的方法-记录用户秒杀成功后生成的订单-并进行异步邮件消息的通知
     *
     * @param itemKill
     * @param userId
     */
    private void commonRecordKillSuccessInfo(ItemKill itemKill, Integer userId) {
        //TODO:记录抢购成功后生成的秒杀订单记录
        ItemKillSuccess entity = new ItemKillSuccess();
        String orderNo=String.valueOf(snowFlake.nextId());
        //雪花算法
        entity.setCode(orderNo);
        //entity.setCode(RandomUtil.generateOrderCode()); //当前时间加随机数
        entity.setItemId(itemKill.getItemId());
        entity.setKillId(itemKill.getId());
        entity.setUserId(userId.toString());
        entity.setStatus(SysConstant.OrderStatus.SuccessNotPayed.getCode().byteValue());
        entity.setCreateTime(DateTime.now().toDate());
        //TODO:学以致用，举一反三 -> 仿照单例模式的双重检验锁写法
        if (itemKillSuccessMapper.countByKillUserId(itemKill.getId(), userId) <= 0) {
            int res = itemKillSuccessMapper.insertSelective(entity);
            if (res > 0) {
                //TODO:进行异步邮件消息的通知=rabbitmq+mail
                rabbitSenderService.sendKillSuccessEmailMsg(orderNo);

                //TODO:入死信队列，用于 “失效” 超过指定的TTL时间时仍然未支付的订单
                //rabbitSenderService.sendKillSuccessOrderExpireMsg(orderNo);
            }
        }
    }


}
