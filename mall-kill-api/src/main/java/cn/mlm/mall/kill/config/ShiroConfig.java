package cn.mlm.mall.kill.config;

import cn.mlm.mall.kill.security.CustomRealm;
import com.google.common.collect.Maps;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author linSir
 * @version V1.0
 * @Description: Shiro通用化配置
 * @Date 2019/12/11 16:50
 */
@Configuration
public class ShiroConfig {

    /**
     * 用户自定的Realm
     *
     * @return
     */
    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm();
    }

    /**
     * 创建一个安全管理器
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager() {
        //默认web安全管理器
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(customRealm());
        return defaultWebSecurityManager;
    }

    /**
     * shiro过滤器
     *
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager());
        //登入请求路径
        filterFactoryBean.setLoginUrl("/kill/to/login");
        //未授权的跳转路径
        filterFactoryBean.setUnauthorizedUrl("/unauth");
        //添加过滤连
        Map<String, String> filterChainDefinitionMap = Maps.newHashMap();
        filterChainDefinitionMap.put("/kill/to/login", "anon");
        filterChainDefinitionMap.put("/**", "anon");
        //需要登入授权访问的请求
        filterChainDefinitionMap.put("/kill/execute/*", "authc");
        filterChainDefinitionMap.put("/item/detail/*", "authc");
        filterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return filterFactoryBean;
    }
}
