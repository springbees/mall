package cn.mlm.mall.kill.service;

import cn.mlm.mall.kill.pojo.ItemKill;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
public interface IItemService {

    /**
     * 查询秒杀列表商品
     *
     * @return
     */
    List<ItemKill> getKillItems();

    /**
     * 秒杀商品详情
     *
     * @param id
     * @return
     */
    ItemKill getKillDetail(Integer id);
}
