package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.service.IKillService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
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
        return iKillService.killItem( killId, userId);
    }
}
