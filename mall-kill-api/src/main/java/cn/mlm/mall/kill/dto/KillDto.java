package cn.mlm.mall.kill.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 20:19
 */
@Data
public class KillDto {
    @NotNull
    private Integer killId;
    /**
     * 默认是从session获取
     */
    private Integer userId;
}
