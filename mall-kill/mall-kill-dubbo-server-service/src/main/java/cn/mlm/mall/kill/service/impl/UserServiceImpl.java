package cn.mlm.mall.kill.service.impl;

import cn.mlm.mall.common.base.BaseServiceImpl;
import cn.mlm.mall.kill.mapper.UserMapper;
import cn.mlm.mall.kill.pojo.User;
import cn.mlm.mall.kill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@Transactional //开启事物
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(int id) {
        System.out.println("id ===================== " + id);
        System.out.println(userMapper.selectById(id));
        return userMapper.selectById(id);
    }
}
