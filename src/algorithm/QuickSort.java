package algorithm;

public class QuickSort {

    public static void main(String[] args) {
        int[] input = new int[] {8,  1,  9,  12,  17,  3,  14,  16,  13,  18,  19,  20,  21,  22,  23,  24,  25,  26,  27,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40};
        quickSort(input, 0, input.length - 1);

        for (int i : input) {
            System.out.print(" "+i);
        }
    }

    private static void quickSort(int[] a, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            int x = a[left];
            while (i < j) {
                while (i < j && a[j] >= x) {
                    j--;
                }
                if (i < j) a[i++] = a[j];
                while (i < j && a[i] < x) {
                    i++;
                }
                if (i < j) a[j--] = a[i];
            }
            a[i] = x;
            quickSort(a, left, i - 1);
            quickSort(a, i + 1, right);
        }
    }
}
