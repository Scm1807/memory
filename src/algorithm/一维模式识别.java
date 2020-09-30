package algorithm;

import jdk.nashorn.internal.runtime.FindProperty;

public class 一维模式识别 {

    public static void main(String[] args) {
        int[] a = {-6,-3,-2,-7,-15,-1,-2,-2,-11};
        int i = FindGreatestSumOfSubArray(a);
        System.out.println(i);
    }
    public static int FindGreatestSumOfSubArray(int[] array) {
        if (array.length == 0) {
            return 0;
        }
        int sum = array[0];
        int maxSum = array[0];
        for (int i = 1; i < array.length; i++) {
            if (sum < 0) { //当sum小于0的时候,不可以用来提升数值大小,所以直接将sum变为下一元素.
                // 大于0的时候就可以补充,所以算进总和里面.
                sum = 0;
            }
            sum += array[i];
            maxSum = Math.max(maxSum, sum);//maxSum用来记录当前运行过程中最大的和向量.
        }
        return maxSum;
    }
}
