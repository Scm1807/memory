package util;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        String fileDirectory = File.separator;

        System.out.println(fileDirectory);
    }


    public static void deleteFile(File file) {
        if (file.exists()) {//判断路径是否存在
            if (file.isFile()) {//boolean isFile():测试此抽象路径名表示的文件是否是一个标准文件。
                file.delete();
            } else {//不是文件，对于文件夹的操作
                File[] listFiles = file.listFiles();//listFiles方法：返回file路径下所有文件和文件夹的绝对路径
                for (File file2 : listFiles) {
                    deleteFile(file2);
                }
            }
            file.delete();
        } else {
            System.out.println("该file路径不存在！！");
        }
    }


}
