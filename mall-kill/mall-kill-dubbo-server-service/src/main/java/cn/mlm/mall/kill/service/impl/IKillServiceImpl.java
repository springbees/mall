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
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.TimeUnit;

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

    private SnowFlake snowFlake = new SnowFlake(2, 3);

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
        String orderNo = String.valueOf(snowFlake.nextId());
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
                rabbitSenderService.sendKillSuccessOrderExpireMsg(orderNo);
            }
        }
    }

    /**
     * 商品秒杀核心业务逻辑的处理-mysql的优化
     *
     * @param killId
     * @param userId
     * @return
     */
    @Override
    public Boolean killItemV2(Integer killId, Integer userId) {
        Boolean result = false;

        //TODO:判断当前用户是否已经抢购过当前商品
        if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
            //TODO:A.查询待秒杀商品详情
            ItemKill itemKill = itemKillMapper.selectByIdV2(killId);

            //TODO:判断是否可以被秒杀canKill=1?
            if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
                //TODO:B.扣减库存-减一
                int res = itemKillMapper.updateKillItemV2(killId);

                //TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
                if (res > 0) {
                    commonRecordKillSuccessInfo(itemKill, userId);
                    result = true;
                }
            }
        } else {
            throw new RuntimeException("您已经抢购过该商品了!");
        }
        return result;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 商品秒杀核心业务逻辑的处理-redis的分布式锁
     *
     * @return
     */
    @Override
    public Boolean killItemV3(Integer killId, Integer userId) {
        Boolean result = false;
        //TODO:借助Redis的原子操作实现分布式锁-对共享操作-资源进行控制
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //创建唯一的Key和Value
        final String key = new StringBuffer().append(killId).append(userId).append("-RedisLock").toString();
        final String value = RandomUtil.generateOrderCode();
        //luna脚本提供“分布式锁服务”，就可以写在一起
        Boolean cacheRes = valueOperations.setIfAbsent(key, value);
        //TOOD:redis部署节点宕机了
        if (cacheRes) {
            //30秒内过期key
            redisTemplate.expire(key, 30, TimeUnit.SECONDS);
            try {
                ItemKill itemKill = itemKillMapper.selectByIdV2(killId);
                if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
                    int res = itemKillMapper.updateKillItemV2(killId);
                    if (res > 0) {
                        commonRecordKillSuccessInfo(itemKill, userId);

                        result = true;
                    }
                }
            } catch (Exception e) {
                log.error("商品秒杀核心业务逻辑的处理-redis的分布式锁-出现异常了: {}", e.fillInStackTrace());
            } finally {
                if (value.equals(valueOperations.get(key).toString())) {
                    redisTemplate.delete(key);
                }
            }
        }
        return result;
    }

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 商品秒杀核心业务逻辑的处理-redisson的分布式锁
     *
     * @param killId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
        Boolean result = false;
        //获取唯一的key
        final String lockKey = new StringBuffer().append(killId).append(userId).append("-RedissonLock").toString();
        //获取Redisson锁
        RLock lock = redissonClient.getLock(lockKey);
        try {
            Boolean cacheRes = lock.tryLock(30, 10, TimeUnit.SECONDS);
            if (cacheRes) {
                //TODO:核心业务逻辑的处理
                if (itemKillSuccessMapper.countByKillUserId(killId, userId) <= 0) {
                    ItemKill itemKill = itemKillMapper.selectByIdV2(killId);
                    if (itemKill != null && 1 == itemKill.getCanKill() && itemKill.getTotal() > 0) {
                        int res = itemKillMapper.updateKillItemV2(killId);
                        if (res > 0) {
                            commonRecordKillSuccessInfo(itemKill, userId);

                            result = true;
                        }
                    }
                } else {
                    throw new Exception("redisson-您已经抢购过该商品了!");
                }
            }
        } finally {
            //释放锁
            lock.unlock();
            //强制释放锁
            //lock.forceUnlock();
        }

        return result;
    }

    @Autowired
    private CuratorFramework curatorFramework;

    private static final String pathPrefix = "/kill/zkLock/";

    /**
     * 商品秒杀核心业务逻辑的处理-基于Zookeeper的分布式锁
     *
     * @param killId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Boolean KillItemV5(Integer killId, Integer userId) throws Exception {
        Boolean result = false;
        InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, pathPrefix + killId + userId + "");

        try {
            //加锁
            if (interProcessMutex.acquire(10L, TimeUnit.SECONDS)) {
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
            }

        } catch (Exception e) {
            throw new Exception("还没到抢购日期、已过了抢购时间或已被抢购完毕！");
        } finally {
            //释放锁
            interProcessMutex.release();
        }
        return result;
    }
}
