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

    /**
     * 查询用户实现
     * @param username
     * @return
     */
    public User selectByUserName(String username) {
        return apiUserService.selectByUserName(username);
    }

    /**
     * 用户登入服务实现
     * @param username
     * @param password
     * @return
     */
    public User selectByUserNameAndPassword(String username, String password) {
        return apiUserService.selectByUserNameAndPassword( username, password);
    }
}
