package cn.mlm.mall.kill.security;

import cn.mlm.mall.kill.pojo.User;
import cn.mlm.mall.kill.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @author linSir
 * @version V1.0
 * @Description: 用户继承Shiro提供的AuthorizingRealm 实现自定义Realm，完成用户的登入授权认证
 * @Date 2019/12/11 16:50
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm {
    @Override
    public String getName() {
        return "CustomRealm";
    }

    private static final Long SESSION_KEY_TIMEOUT = 3600_000L;
    @Autowired
    private Environment env;
    /**
     * 实现数据库查询提供服务接口
     */
    @Autowired
    private UserService userService;


    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取token
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //从token获取帐号和密码
        log.info("当前登入帐号={},当前登入的密码={}", token.getUsername(), token.getPassword());
        //根据用户名查找是否存在该用户
        User user = userService.selectByUserName(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException("用户名不存在！");
        }
        //查询当前用户是否已经被禁用
        if (!Objects.equals(1, user.getIsActive().intValue())) {
                throw new DisabledAccountException("用户已经被禁用，请联系客服！");
        }
        //根据用户名和密码进行查询判断该用户帐号密码是都验证成功
        String newPsd=new Md5Hash(String.valueOf(token.getPassword()),env.getProperty("shiro.encrypt.password.salt")).toString();
        if (!user.getPassword().equals(newPsd)) {
            throw new IncorrectCredentialsException("用户名密码不匹配!");
        }
        //创建身份验证信息对象
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(), String.valueOf(token.getPassword()), getName());
        //将用户ID存放在session中用于其他地方调用
        setSession("uid", user.getId());
        return info;
    }

    /**
     * 将key与对应的value塞入shiro的session中-最终交给HttpSession进行管理
     * (如果是分布式session配置，那么就是交给redis管理)
     *
     * @param key
     * @param value
     */
    private void setSession(String key, Integer value) {
        //从shiro中获取session
        Session session = SecurityUtils.getSubject().getSession();
        //将刚刚信息存入session中
        session.setAttribute(key, value);
        //设置session有效时间
        session.setTimeout(SESSION_KEY_TIMEOUT);
    }
}
