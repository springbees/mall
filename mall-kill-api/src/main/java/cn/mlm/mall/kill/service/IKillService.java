package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.api.ApiIKillService;
import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:15
 * 秒杀调用dubbo服务
 */
@Service
public class IKillService {

    @Reference(version = "${dubbo.service.version}")
    private ApiIKillService apiIKillService;

    public Boolean killItem(Integer killId, Integer userId) {
        return apiIKillService.killItem(killId, userId);
    }

    /**
     * 数据库优化接口
     *
     * @param killId
     * @param userId
     * @return
     */
    public Boolean killItemV2(Integer killId, Integer userId) {
        return apiIKillService.killItemV2(killId, userId);
    }

    /**
     * redis分布式锁
     *
     * @param killId
     * @param userId
     * @return
     */
    public Boolean killItemV3(Integer killId, Integer userId) {
        return apiIKillService.killItemV3(killId, userId);
    }

    /**
     * Redisson分布式锁
     *
     * @param killId
     * @param userId
     * @return
     */
    public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
        return apiIKillService.killItemV4(killId, userId);
    }

    /**
     * 基于Zookeeper分布式锁
     *
     * @param killId
     * @param userId
     * @return
     */
    public Boolean killItemV5(Integer killId, Integer userId) throws Exception {
        return apiIKillService.killItemV5(killId, userId);
    }

    /**
     * 查看抢购订单详情
     *
     * @param orderNo
     * @return
     */
    public KillSuccessUserInfo selectByCode(String orderNo) {
        return apiIKillService.selectByCode(orderNo);
    }

    public int updateByOderCode(String oderCode) {
        return apiIKillService.updateByOderCode( oderCode) ;
    }
}
