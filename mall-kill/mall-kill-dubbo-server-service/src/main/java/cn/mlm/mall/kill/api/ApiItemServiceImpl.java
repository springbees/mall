package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.ItemKill;
import cn.mlm.mall.kill.service.IItemService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/2 19:46
 * 发布服务
 */
@Transactional //开启事物
@Service(version = "${dubbo.service.version}")
public class ApiItemServiceImpl implements ApiItemService {

    @Autowired
    private IItemService iItemService;
    @Override
    public List<ItemKill> getKillItems() throws Exception {
        return iItemService.getKillItems();
    }

    @Override
    public ItemKill getKillDetail(Integer id) {
        return iItemService.getKillDetail(id);
    }
}
