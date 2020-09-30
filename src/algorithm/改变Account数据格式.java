package algorithm;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class 改变Account数据格式 {

    public static void main(String[] args) {
        File file1 = new File("C:/aaa/c.txt");
        File file2 = new File("C:/aaa/c.txt");
        changeCount(file1);
    }

    private static void changeCount(File inputFile) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(inputFile));
            String str;
            while ((str = bufferedReader.readLine()) != null) {

                Map<String, Double> monthMap = new HashMap<>();
                for (int i = 1; i <= 12; i++) {
                    monthMap.put(i+"月", 0.00);
                }
                Map<String, Map<String, Double>> account = new HashMap<>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

