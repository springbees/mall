package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.service.IKillService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:33
 * 发布dubbo服务
 */
@Service(version = "${dubbo.service.version}")
public class ApiIKillServiceImpl implements ApiIKillService {

    @Autowired
    private IKillService iKillService;

    @Override
    public Boolean killItem(Integer killId, Integer userId) {
        return iKillService.killItem(killId, userId);
    }

    @Override
    public Boolean killItemV2(Integer killId, Integer userId) {
        return iKillService.killItemV2(killId, userId);
    }

    @Override
    public Boolean killItemV3(Integer killId, Integer userId) {
        return iKillService.killItemV3(killId, userId);
    }

    @Override
    public Boolean killItemV4(Integer killId, Integer userId) throws Exception {
        return iKillService.killItemV4(killId, userId);
    }

    @Override
    public Boolean killItemV5(Integer killId, Integer userId) throws Exception {
        return iKillService.KillItemV5(killId, userId);
    }

}
