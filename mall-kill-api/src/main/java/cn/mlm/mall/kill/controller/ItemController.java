package cn.mlm.mall.kill.controller;

import cn.mlm.mall.kill.pojo.ItemKill;
import cn.mlm.mall.kill.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 20:26
 * 秒杀商品，商品列表controller
 */
@Controller
public class ItemController {
    public static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    /**
     * 获取商品列表
     */
    @RequestMapping(value = {"/", "/index", "/list", "/index.html"}, method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        try {
            //获取待秒杀商品列表
            List<ItemKill> list = itemService.list();
            modelMap.addAttribute("itemKills", list);
            log.info("获取待秒杀商品列表-数据：{}", list);
        } catch (Exception e) {
            log.error("获取待秒杀商品列表-发生异常：", e.fillInStackTrace());
            return "redirect:/base/500";
        }
        return "index";
    }

    /**
     * 获取秒杀商品的详情
     *
     * @param id       商品ID
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/item/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Integer id, ModelMap modelMap) {
        if (id == null || id <= 0) {
            return "redirect:/base/500";
        }
        try {
            //获取待秒杀商品列表
            List<ItemKill> list = itemService.list();
            modelMap.addAttribute("itemKills", list);
            //获取秒杀商品数据对象
            ItemKill detail=itemService.getKillDetail(id);
            modelMap.put("detail",detail);
            log.info("获取待秒杀商品详情-数据：{}", detail);
        } catch (Exception e) {
            log.error("获取待秒杀商品的详情-发生异常：id={}", id, e.fillInStackTrace());
            return "redirect:/base/500";
        }
        return "details";
    }
}
