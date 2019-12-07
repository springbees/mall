package cn.mlm.mall.common.base;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public abstract class BasePojo implements java.io.Serializable {

    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
