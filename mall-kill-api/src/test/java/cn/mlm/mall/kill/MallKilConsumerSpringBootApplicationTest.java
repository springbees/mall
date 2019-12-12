package cn.mlm.mall.kill;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author linSir
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @Date 2019/12/11 20:45
 */
public class MallKilConsumerSpringBootApplicationTest {
        public static void main(String[] args) {
        String salt="11299c42bf954c0abb373efbae3f6b26";
        String password="admin";
        System.out.println(new Md5Hash(password,salt));
    }
}
