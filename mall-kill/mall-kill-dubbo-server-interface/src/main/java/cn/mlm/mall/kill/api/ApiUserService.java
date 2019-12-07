package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.User;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 15:33
 */
public interface ApiUserService {
    User selectById(int id);
}
