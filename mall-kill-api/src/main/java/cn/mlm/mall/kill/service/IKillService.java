package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.api.ApiIKillService;
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
}
