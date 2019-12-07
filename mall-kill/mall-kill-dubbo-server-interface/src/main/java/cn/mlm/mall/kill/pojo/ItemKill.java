package cn.mlm.mall.kill.pojo;

import cn.mlm.mall.common.base.BasePojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 待秒杀商品表
 * </p>
 *
 * @author jacklin
 * @since 2019-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ItemKill extends BasePojo {

    private static final long serialVersionUID = 1L;

    private Integer id;
    /**
     * 商品id
     */
    private Integer itemId;

    /**
     * 可被秒杀的总数
     */
    private Integer total;

    /**
     * 秒杀开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;

    /**
     * 商品名
     */
    private String itemName;


    //采用服务器时间控制是否可以进行抢购
    private Integer canKill;

    /**
     * 秒杀结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;

    /**
     * 是否有效（1=是；0=否）
     */
    private Integer isActive;
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

}
