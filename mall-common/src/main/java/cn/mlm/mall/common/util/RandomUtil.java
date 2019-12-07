package cn.mlm.mall.common.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Class class file creation author：jakclinsir
 * @DATE 2019/12/3 19:51
 */
public class RandomUtil {

    //生成当前时间 格式yyyMMddHHmmssSS
    private static final SimpleDateFormat dateFormatOne = new SimpleDateFormat("yyyMMddHHmmssSS");
    //线程安全的随机数
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * 生成订单编号-方式一
     *
     * @return
     */
    public static String generateOrderCode() {
        //TODO:时间戳+N为随机数流水号
        return dateFormatOne.format(DateTime.now().toDate()) + generateNumber(4);
    }

    /**
     * 生成一个随机数
     *
     * @return
     */
    private static String generateNumber(final int num) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= num; i++) {
            sb.append(random.nextInt(9));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            System.out.println(RandomUtil.generateOrderCode());
        }
    }
}
