package kthis;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeFileString {

    /**
     * 替换文件中的字符串
     *
     * @param oldfile
     * @param newFile
     */
    private static void alterStrToNewFile(File oldfile, File newFile) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(oldfile))); //创建对目标文件读取流
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(newFile, true))); //创建对临时文件输出流，并追加

            String string; //存储对目标文件读取的内容
            while ((string = br.readLine()) != null) {
                Pattern p = Pattern.compile("\\bMessages\\.packageCard_\\w+\\b");
                Matcher m = p.matcher(string);
                //判断读取的内容是否包含原字符串
                while (m.find()) {
                    //替换读取内容中的原字符串为新字符串, replaceAll 替换所有匹配项
                    String str = m.group().substring(9); //可以获取匹配的变量名
                    string = m.replaceAll("Messages.getString(\"" + str + "\")"); //group方法返回由以前匹配操作所匹配的输入子序列。
                }
                bw.write(string);
                bw.newLine(); //添加换行
            }
            br.close(); //关闭流，对文件进行删除等操作需先关闭文件流操作
            bw.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 遍历需要修改的文件
     *
     * @param oldPath 存放替换之前文件的路径
     * @param newPath 存放替换之后文件的路径
     */
    private static void ergodicFile(String oldPath, String newPath) {
        File oldPathFile = new File(oldPath);

        if (oldPathFile.exists()) {
            File[] files = oldPathFile.listFiles();
            if (null == files || files.length == 0) {
                System.out.println(oldPath + "文件夹是空的!");
            } else {
                for (File file : files) {
                    if (file.isDirectory()) {
                        //ergodicFile(file.getAbsolutePath(), newPath);
                    } else {
                        try {
                            File newFile = new File(newPath + "\\" + file.getName()); //创建临时文件
                            if (!newFile.exists()) {
                                newFile.createNewFile();//不存在则创建
                                System.out.println("创建文件:" + newFile.getAbsolutePath());
                            }
                            // 更改字符串并保存到新创建的文件当中
                            alterStrToNewFile(file, newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        } else {
            System.out.println(oldPath + "文件夹不存在!");
        }
    }

    private static File appendStr(String oldFile, String regex) {
        return null;
    }

    public static void main(String[] args) {
        String oldPath = "C:\\Users\\ihis\\Desktop\\a";
        String newPath = "C:\\Users\\ihis\\Desktop\\a\\b";
        ergodicFile(oldPath, newPath);
    }
}
