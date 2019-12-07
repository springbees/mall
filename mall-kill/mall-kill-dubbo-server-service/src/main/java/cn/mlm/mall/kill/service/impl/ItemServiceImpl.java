package cn.mlm.mall.kill.service.impl;

import cn.mlm.mall.common.base.BaseServiceImpl;
import cn.mlm.mall.kill.mapper.ItemKillMapper;
import cn.mlm.mall.kill.mapper.ItemMapper;
import cn.mlm.mall.kill.pojo.Item;
import cn.mlm.mall.kill.pojo.ItemKill;
import cn.mlm.mall.kill.service.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@Transactional
@Service
public class ItemServiceImpl extends BaseServiceImpl<ItemKill> implements IItemService {

    @Autowired
    private ItemKillMapper itemKillMapper;

    @Override
    public List<ItemKill> getKillItems() {
        return itemKillMapper.getKillItems();
    }

    @Override
    public ItemKill getKillDetail(Integer id) {
        return itemKillMapper.getKillDetail(id);
    }
}
