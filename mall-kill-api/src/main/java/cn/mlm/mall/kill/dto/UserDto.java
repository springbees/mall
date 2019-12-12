package cn.mlm.mall.kill.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author linSir
 * @version V1.0
 * @Description: 用户登入的参数封装
 * @Date 2019/12/11 17:08
 */
@Data
public class UserDto {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
