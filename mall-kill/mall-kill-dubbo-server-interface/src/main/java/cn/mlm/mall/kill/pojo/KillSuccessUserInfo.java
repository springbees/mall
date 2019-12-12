package cn.mlm.mall.kill.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author linSir
 */
@Data
public class KillSuccessUserInfo extends ItemKillSuccess implements Serializable {
    private String userName;

    private String phone;

    private String email;

    private String itemName;
    private String imgUrl;
    private BigDecimal killPrice;

    @Override
    public String toString() {
        return super.toString() + "\nKillSuccessUserInfo{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
