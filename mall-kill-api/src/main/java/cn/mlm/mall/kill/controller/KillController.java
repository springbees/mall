package cn.mlm.mall.kill.controller;

import cn.mlm.mall.common.CommonResult;
import cn.mlm.mall.kill.dto.KillDto;
import cn.mlm.mall.kill.pojo.ItemKill;
import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;
import cn.mlm.mall.kill.service.IKillService;
import com.alibaba.dubbo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static cn.mlm.mall.common.ResultCode.KILL_FAILED;
import static cn.mlm.mall.common.ResultCode.VALIDATE_FAILED;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:11
 * 秒杀controller
 */
@Controller
@RequestMapping("kill")
public class KillController {
    private static final Logger log = LoggerFactory.getLogger(KillController.class);

    @Autowired
    private IKillService killService;


    /**
     * 订单秒杀方法
     *
     * @return
     */
    @RequestMapping(value = "/execute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CommonResult killExecute(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session) {
        if (result.hasErrors() || dto.getKillId() <= 0) {
            //参数校验失败
            return CommonResult.failed(VALIDATE_FAILED);
        }
        try {
            Integer uId = (Integer) session.getAttribute("uid");
            if (uId == null) {
                return CommonResult.failed();
            }
            //基于Redisson的分布式锁进行控制 传入用户秒杀商品ID和用户ID
            Boolean res = killService.killItemV4(dto.getKillId(), uId);
            if (!res) {
                return CommonResult.failed("基于Redisson的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(null, "抢购成功！");
    }


    /**
     * 秒杀成功查看订单详情
     *
     * @param orderNo 用户秒杀成功的订单号
     * @return
     */
    @RequestMapping(value = "/kill/record/detail/{orderNo}", method = RequestMethod.GET)
    public String recordDetail(@PathVariable String orderNo, ModelMap modelMap) {
        if (StringUtils.isBlank(orderNo)) {
            return "fail";
        }
        KillSuccessUserInfo info = killService.selectByCode(orderNo);
        if (info == null) {
            return "fail";
        }
        modelMap.put("info", info);
        return "order";
    }

    @RequestMapping(value = "/record/cancelOrder/{oderCode}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult cancelOrder(@PathVariable String oderCode) {
        if (StringUtils.isBlank(oderCode)) {
            return CommonResult.failed("取消订单失败");
        }
        int item = killService.updateByOderCode(oderCode);
        log.info("取消的订单操作结果:{}", item);
        if (item <= 0) {
            return CommonResult.failed("取消订单失败");
        }
        return CommonResult.success("取消成功");
    }

    /**
     * 抢购成功跳转页面
     *
     * @return
     */
    @RequestMapping("/execute/success")
    public String success() {
        return "executeSuccess";
    }

    /**
     * 抢购失败跳转页面
     *
     * @return
     */
    @RequestMapping("/execute/fail")
    public String fail() {
        return "fail";
    }


    /**
     * 订单秒杀方法
     *
     * @return
     */
    @RequestMapping(value = "/jmeterKillExecute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CommonResult killExecuteJmeter(@RequestBody @Validated KillDto dto, BindingResult result, HttpSession session) {
        if (result.hasErrors() || dto.getKillId() <= 0) {
            //参数校验失败
            return CommonResult.failed(VALIDATE_FAILED);
        }
        try {
            //不加分布式锁的前提
            /*Boolean res=killService.killItemV2(dto.getKillId(),dto.getUserId());
            if (!res){
                return  CommonResult.failed("不加分布式锁-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
            }*/

            //基于Redis的分布式锁进行控制
            /*Boolean res=killService.killItemV3(dto.getKillId(),dto.getUserId());
            if (!res){
                return  CommonResult.failed("基于Redis的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
            }*/

            //基于Redisson的分布式锁进行控制
            /*Boolean res=killService.killItemV4(dto.getKillId(),dto.getUserId());
            if (!res){
                 return  CommonResult.failed("基于Redisson的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
            }*/

            //基于ZooKeeper的分布式锁进行控制
            Boolean res = killService.killItemV5(dto.getKillId(), dto.getUserId());
            if (!res) {
                return CommonResult.failed("基于ZooKeeper的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success(null, "抢购成功！");
    }
}
