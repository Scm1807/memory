package com.company;

import java.util.concurrent.ConcurrentHashMap;

public class Test2 implements Test1 {
    @Override
    public void out() {
        System.out.println("ddddd");
        System.out.println("ddddd");

        ConcurrentHashMap m = new ConcurrentHashMap();
        m.put("","");

        /*
        服务化（各系统之间相互调用混乱；会产生代码重复，相互通信问题）
         */
    }
}
