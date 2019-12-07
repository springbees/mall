package cn.mlm.mall.kill.controller;


import cn.mlm.mall.kill.pojo.User;
import cn.mlm.mall.kill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器 模板
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public User index(@RequestParam(value = "id", defaultValue = "10") int id) {
        return userService.selectById(id);

    }
}
