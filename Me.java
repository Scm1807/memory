package com.company;

import java.math.BigDecimal;
public class Me <T> {


    public static final BigDecimal BIGDECIMAL_0 = new BigDecimal(0);
    public static final BigDecimal BIGDECIMAL_1 = new BigDecimal(1);
    public static final BigDecimal BIGDECIMAL_60 = new BigDecimal(60);

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal("10.0091");
        bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
        String s = hoursTime2MinutesTime(bigDecimal);
        System.out.println(s);
    }


    public static String hoursTime2MinutesTime(BigDecimal hoursTime) {
        String time = "";

        if (hoursTime == null || hoursTime.compareTo(BIGDECIMAL_0) <= 0) {
            return time;
        }
        //如果大于0小于1小时
        if (hoursTime.compareTo(BIGDECIMAL_0) > 0 && hoursTime.compareTo(BIGDECIMAL_1) < 0) {
            time = hoursTime.multiply(BIGDECIMAL_60).setScale(0,BigDecimal.ROUND_HALF_UP).toString() + "分钟";
        } else if (hoursTime.compareTo(BIGDECIMAL_1) >= 0) {
            //得到向下取整的小时数
            BigDecimal downTime = hoursTime.setScale(0,BigDecimal.ROUND_DOWN);

            //如果正好为小时整
            if (hoursTime.compareTo(downTime) == 0) {
                time = downTime.toString() + "小时";
            }else {
                time = downTime.toString() + "小时" +
                        hoursTime.subtract(downTime).multiply(BIGDECIMAL_60).setScale(0,BigDecimal.ROUND_HALF_UP).toString() + "分钟";
            }
        }
        return time;
    }
}