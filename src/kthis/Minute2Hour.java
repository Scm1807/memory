package kthis;

import java.math.BigDecimal;

/**
 * 已上传
 **/
public class Minute2Hour {

    private static final BigDecimal BIGDECIMAL_60 = new BigDecimal(60);

    private static String hoursTime2MinutesTime(BigDecimal hoursTime) {
        String time = "";

        // 如果不符合直接返回空字符
        if (hoursTime == null || hoursTime.compareTo(BigDecimal.ZERO) <= 0) {
            return time;
        }
        // 设置精度为2位小数, 以防出现x小时0分钟或者x小时60分钟等bug, 因为乘60之后会使数据量级变大
        hoursTime = hoursTime.setScale(2, BigDecimal.ROUND_HALF_UP);
        //如果大于等于0小于1小时
        if (hoursTime.compareTo(BigDecimal.ZERO) >= 0 && hoursTime.compareTo(BigDecimal.ONE) < 0) {
            time = hoursTime.multiply(BIGDECIMAL_60).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + "分钟";
        } else if (hoursTime.compareTo(BigDecimal.ONE) >= 0) { // 如果大于1小时
            //得到向下取整的小时数
            BigDecimal downTime = hoursTime.setScale(0, BigDecimal.ROUND_DOWN);

            //如果正好为小时整
            if (hoursTime.compareTo(downTime) == 0) {
                time = downTime.toString() + "小时";
            } else {
                time = downTime.toString() + "小时"
                        + hoursTime.subtract(downTime).multiply(BIGDECIMAL_60).setScale(0, BigDecimal.ROUND_HALF_UP).toString()
                        + "分钟";
            }
        }
        return time;
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("10.005");
        String s = hoursTime2MinutesTime(bigDecimal);
        System.out.println(s); // 10小时1分钟

        BigDecimal bigDecimal1 = new BigDecimal("10.0049");
        String s1 = hoursTime2MinutesTime(bigDecimal1);
        System.out.println(s1); // 10小时

        BigDecimal bigDecimal2 = new BigDecimal("10.994");
        String s2 = hoursTime2MinutesTime(bigDecimal2);
        System.out.println(s2); // 10小时59分钟

        BigDecimal bigDecimal3 = new BigDecimal("10.995");
        String s3 = hoursTime2MinutesTime(bigDecimal3);
        System.out.println(s3); // 11小时

        BigDecimal bigDecimal4 = new BigDecimal("0.0049");
        String s4 = hoursTime2MinutesTime(bigDecimal4);
        System.out.println(s4); // 0分钟
    }

}