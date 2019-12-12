package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.pojo.User;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
public interface IUserService {
    User selectById(int id);

    /**
     * 根据帐号密码查询对象
     *
     * @param username
     * @param password
     * @return
     */
    User selectByUserNameAndPassword(String username, String password);

    /**
     * 根据帐号查询对象
     *
     * @param username
     * @return
     */
    User selectByUserName(String username);
}
