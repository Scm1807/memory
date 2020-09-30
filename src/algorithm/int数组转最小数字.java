package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class int数组转最小数字 {
    public static void main(String[] args) {

        int[] a = {1, 44, 66, 13, 11115, 22, 12};
        System.out.println(intArrToLong(a));
    }

    private static String intArrToLong(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int i : arr) {
            list.add(i);
        }

        //普通方法
//        Collections.sort(list, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return (o1 + "").compareTo(o2 + "");
//            }
//        });


        //lambda表达式方法
        //Collections.sort(list, (o1, o2) -> (o1 + "").compareTo(o2 + ""));
        //Collections.sort(list, Comparator.comparing(o -> (o + "")));
        list.sort(Comparator.comparing(o -> (o + "")));//最好的方法

        StringBuilder s = new StringBuilder();
        for (Integer integer : list) {
            s.append(integer);
        }
        return s.toString();
    }
}
