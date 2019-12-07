package cn.mlm.mall.kill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 18:01
 */
@Controller
public class IndexController {

    @RequestMapping("/base/500")
   public String erro(){
        return "500";
   }
}
