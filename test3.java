package com.company;


import org.dom4j.DocumentException;

import java.lang.reflect.Proxy;
import java.text.ParseException;

public class test3{
    public static  void main(String[] args) throws DocumentException, ParseException {
//        System.out.println("请输入年月日时分秒（格式：2017年09月30日23时59分59秒）:");
//        //接收用户输入一个日期
//        Scanner scan=new Scanner(System.in);
//        String before=scan.next();
//        // /* * 将用户输入的字符串转化为日期类型并格式化 * 如果设置为"yyyy/MM/dd HH:mm:ss"这种格式，空格后面的时分秒无法读取，运行时会报错 */
//        SimpleDateFormat fmt=
//                new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
//        Calendar calendar=Calendar.getInstance();
//           calendar.setTime(fmt.parse(before));
//        //加1秒
//        calendar.add(Calendar.SECOND, 1);
//        //输出2017年10月01日00时00分00秒
//        System.out.println("加1秒后的时间为："+fmt.format(calendar.getTime()));
//        String s = "";
//        s.hashCode();
//        System.out.println(Test.class.getName());
        ProxyInvocationHandler handler = new ProxyInvocationHandler(Test1.class, null, null, null);
        Class[] interfaces = new Class[] { Test1.class };
        Test1 s = (Test1) Proxy.newProxyInstance(Test1.class.getClassLoader(), interfaces, handler);
        s.out();
    }
}