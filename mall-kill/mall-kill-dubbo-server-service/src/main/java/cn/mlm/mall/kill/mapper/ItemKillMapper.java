package cn.mlm.mall.kill.mapper;

import cn.mlm.mall.kill.pojo.ItemKill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 待秒杀商品表 Mapper 接口
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
public interface ItemKillMapper extends BaseMapper<ItemKill> {

    /**
     * 获取秒杀商品列表
     *
     * @return
     */
    List<ItemKill> getKillItems();

    /**
     * 获取秒杀商品详情
     *
     * @param id
     * @return
     */
    ItemKill getKillDetail(@Param("id") Integer id);

    /**
     * 库存-1
     *
     * @param killId
     * @return
     */
    int updateKillItem(@Param("killId") Integer killId);

    int restoreItemInventory(@Param("itemId") Integer itemId);

    /**
     * 秒杀商品详情数据库优化
     * @param killId
     * @return
     */
    ItemKill selectByIdV2(@Param("killId")Integer killId);

    /**
     * 更新商品库存数量，保证数据库total大于0才更新
     * @param killId
     * @return
     */
    int updateKillItemV2(@Param("killId")Integer killId);
}
