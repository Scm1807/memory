package algorithm;

public class 快排 {
    public static void main(String[] args) {

        int[] a = {1, 4, 2, 5, 6, 334, 4, 5, 8, 9, 66, 5, 44, 6, 6, 4, 22, 11, 23, 4, 5, 6, 67, 7, 78,};
        quickSort(a, 0, a.length - 1);
        for (int i : a) {
            System.out.println(i);
        }
    }

    //快速排序
    private static void quickSort(int[] arr, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            int x = arr[left];
            while (i < j) {
                while (i < j && arr[j] >= x) {
                    j--;
                }
                if (i < j) {
                    arr[i++] = arr[j];

                }
                while (i < j && arr[i] < x) {
                    i++;
                }
                if (i < j) {
                    arr[j--] = arr[i];
                }
            }

            arr[j] = x;
            quickSort(arr, left, i - 1);
            quickSort(arr, i + 1, right);
        }
    }
}
