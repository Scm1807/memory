package com.company;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

public class Test {

    public static final BigDecimal BIGDECIMAL_60 = new BigDecimal(60);
    public static <T extends Throwable> void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, T {


        System.out.printf("%8.3f | %1.4f%n",111.11979,222.22222);
        System.out.printf("%4.4f | %1.4f",111.11979,222.22222);
        System.out.println();
        String s = hoursTime2MinutesTime(new BigDecimal("2.009"));
        System.out.println(s);


    }

    void out() {
        System.out.println("sssssssssssss");
    }
    public static String hoursTime2MinutesTime(BigDecimal hoursTime) {
        String time = "";

        if (hoursTime == null ) {
            return time;
        }
        //设置2位小数,否则会出现x小时0分钟和x小时60分钟等bug
        hoursTime = hoursTime.setScale(2,BigDecimal.ROUND_HALF_UP);
        //数值为负数或者太小,不符直接忽略,而不输出0分钟
        if (hoursTime.compareTo(BigDecimal.ZERO) <= 0) {
            return time;
        }
        //如果大于0小于1小时
        if (hoursTime.compareTo(BigDecimal.ZERO) > 0 && hoursTime.compareTo(BigDecimal.ONE) < 0) {
            time = hoursTime.multiply(BIGDECIMAL_60).setScale(0,BigDecimal.ROUND_HALF_UP).toString() +"分钟";
        } else if (hoursTime.compareTo(BigDecimal.ONE) >= 0) {
            //得到向下取整的小时数
            BigDecimal downTime = hoursTime.setScale(0,BigDecimal.ROUND_DOWN);

            //如果正好为小时整
            if (hoursTime.compareTo(downTime) == 0) {
                time = downTime.toString() + "小时";
            }else {
                time = downTime.toString() +"小时" +
                        hoursTime.subtract(downTime).multiply(BIGDECIMAL_60).setScale(0,BigDecimal.ROUND_HALF_UP).toString() +"分钟";
            }
        }
        return time;
    }
}

