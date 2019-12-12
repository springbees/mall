package cn.mlm.mall.kill.mapper;

import cn.mlm.mall.kill.pojo.Item;
import cn.mlm.mall.kill.pojo.ItemKillSuccess;
import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linSir
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:38
 */
public interface ItemKillSuccessMapper extends BaseMapper<ItemKillSuccess> {
    int countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);

    int insertSelective(ItemKillSuccess entity);

    KillSuccessUserInfo selectId(@Param("orderNo") String orderNo);

    int  expireOrder(@Param("code") String code);

    ItemKillSuccess queryById(@Param("code") String code);

    List<ItemKillSuccess> selectExpireOrders();

    KillSuccessUserInfo selectByCode(@Param("orderNo") String orderNo);

    int updateByOderCode(@Param(("oderCode")) String oderCode);
}
