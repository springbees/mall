package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.User;
import cn.mlm.mall.kill.service.IUserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 14:58
 */
@Service(version = "${dubbo.service.version}")
public class ApiUserServiceImpl implements ApiUserService {
    @Autowired
    private IUserService userService;
    @Override
    public User selectById(int id) {
        return userService.selectById(id);
    }
}
