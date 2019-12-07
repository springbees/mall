package cn.mlm.mall.kill.pojo;

import cn.mlm.mall.common.base.BasePojo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Item extends BasePojo {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 秒杀价格
     */
    private BigDecimal killPrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;
    /**
     * 商品图片
     */
    private String imgUrl;
    /**
     * 库存
     */
    private Long stock;

    /**
     * 采购时间
     */
    private Date purchaseTime;

    /**
     * 是否有效（1=是；0=否）
     */
    private Integer isActive;



}
