package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.Item;
import cn.mlm.mall.kill.pojo.ItemKill;

import java.util.List;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 19:48
 * 定义api接口
 */
public interface ApiItemService {

    List<ItemKill> getKillItems() throws Exception;

    ItemKill getKillDetail(Integer id);
}
