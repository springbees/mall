package cn.mlm.mall.kill.api;

import cn.mlm.mall.kill.pojo.KillSuccessUserInfo;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:33
 */
public interface ApiIKillService {
    Boolean killItem(Integer killId, Integer userId);

    Boolean killItemV2(Integer killId, Integer userId);

    Boolean killItemV3(Integer killId, Integer userId);

    Boolean killItemV4(Integer killId, Integer userId) throws Exception;

    Boolean killItemV5(Integer killId, Integer userId) throws Exception;

    KillSuccessUserInfo selectByCode(String orderNo);

    int updateByOderCode(String oderCode);
}
