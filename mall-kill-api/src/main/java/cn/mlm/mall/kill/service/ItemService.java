package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.api.ApiItemService;
import cn.mlm.mall.kill.pojo.Item;
import cn.mlm.mall.kill.pojo.ItemKill;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 20:29
 * 获取dubbo服务
 */
@Service
public class ItemService {

    @Reference(version = "${dubbo.service.version}")
    private ApiItemService apiItemService;

    public List<ItemKill> list() throws Exception {
        return apiItemService.getKillItems();
    }

    public ItemKill getKillDetail(Integer id) {
        return apiItemService.getKillDetail(id);
    }
}
