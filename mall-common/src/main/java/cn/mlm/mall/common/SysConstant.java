package cn.mlm.mall.common;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 21:04
 * 订单状态枚举类型
 */
public class SysConstant {

    public enum OrderStatus{

        Invalid(-1,"无效"),
        SuccessNotPayed(0,"成功-未付款"),
        HasPayed(1,"已付款"),
        Cancel(2,"已取消"),

        ;

        private Integer code;
        private String msg;

        OrderStatus(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
