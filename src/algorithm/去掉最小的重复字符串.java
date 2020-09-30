package algorithm;

import java.util.*;

public class 去掉最小的重复字符串 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一串英文字符串：");
        String str = scanner.nextLine();

        char[] strArr = str.toCharArray();

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            map.put(strArr[i] + "", map.containsKey(strArr[i] + "") ? map.get(strArr[i] + "") + 1 : 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(new MyComparetor());
        int min = list.get(0).getValue();
        for (Map.Entry<String, Integer> aList : list) {
            if (aList.getValue().equals(min)) {
                for (int j = 0; j < str.length(); j++) {
                    if (str.contains(aList.getKey())) {
                        str = str.replaceAll(aList.getKey(), "");
                        break;
                    }
                }
            }
        }
        System.out.println(str);
    }
}

class MyComparetor implements Comparator<Map.Entry<String, Integer>> {
    @Override
    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
