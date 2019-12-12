package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.User;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 15:33
 */
public interface ApiUserService {
    User selectById(int id);

    /**
     * 根据用户帐号和密码查询对象
     *
     * @param username
     * @param password
     * @return
     */
    User selectByUserNameAndPassword(String username, String password);

    /**
     * 根据用户帐号查询对象
     *
     * @param username
     * @return
     */
    User selectByUserName(String username);
}
