package cn.mlm.mall.kill.controller;


import cn.mlm.mall.common.CommonResult;
import cn.mlm.mall.kill.dto.UserDto;
import cn.mlm.mall.kill.pojo.User;
import cn.mlm.mall.kill.service.UserService;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.mlm.mall.common.ResultCode.SUCCESS;
import static cn.mlm.mall.common.ResultCode.VALIDATE_FAILED;

/**
 * <p>
 * 用户登入控制器
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@Controller
@RequestMapping("kill")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private UserService userService;

    /**
     * 跳转登入页面
     *
     * @return
     */
    @RequestMapping(value = {"/to/login", "/unauthorized"})
    public String toLogin() {
        return "login";
    }

    /**
     * 处理登入
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CommonResult login(@RequestBody UserDto userDto, ModelMap modelMap) {
        try {
            //处理登入业务
            //判断是否已经认证，如果已经认证就跳过认证这个条件
            if (!SecurityUtils.getSubject().isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken(userDto.getUserName(), userDto.getPassword());
                //进行认证
                SecurityUtils.getSubject().login(token);
            }

        } catch (UnknownAccountException e) {
            modelMap.addAttribute("userName", userDto.getUserName());
            return CommonResult.failed(e.getMessage());
        } catch (DisabledAccountException e) {
            modelMap.addAttribute("userName", userDto.getUserName());
            return CommonResult.failed(e.getMessage());
        } catch (IncorrectCredentialsException e) {
            modelMap.addAttribute("userName", userDto.getUserName());
            return CommonResult.failed(e.getMessage());
        } catch (Exception e) {
            modelMap.addAttribute("userName", userDto.getUserName());
            return CommonResult.failed("用户登录异常，请联系管理员!");
        }
        return CommonResult.success(SUCCESS, "登入成功");
    }
}
