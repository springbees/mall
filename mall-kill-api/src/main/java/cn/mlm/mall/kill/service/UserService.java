package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.api.ApiUserService;
import cn.mlm.mall.kill.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 15:27
 */
@Service
public class UserService {

    @Reference(version = "${dubbo.service.version}")
    private ApiUserService apiUserService;

    public User selectById(int id) {
        return apiUserService.selectById(id);
    }
}
