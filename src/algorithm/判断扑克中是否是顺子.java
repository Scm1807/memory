package algorithm;

import java.util.HashSet;
import java.util.Set;

public class 判断扑克中是否是顺子 {

    public static void main(String[] args) {
        int[] a = {1, 2, 4, 0, 5};
        boolean flag = isContinuous(a);
        System.out.println(flag);
    }

    private static boolean isContinuous(int[] array) {
        if (array.length != 5) {
            return false;
        }

        //ans用来记录0的个数
        int max = -1;
        int min = 100;
        int ans = 0;
        Set<Integer> hashSet = new HashSet<>();

        for (int anArray : array) {
            if (anArray != 0) {
                max = Math.max(max, anArray);
                min = Math.min(min, anArray);
                hashSet.add(anArray);
            } else ans++;
        }
        return ans + hashSet.size() == 5 && (max - min) < 5;
    }
}
