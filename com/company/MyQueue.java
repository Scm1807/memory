package com.company;

import java.util.ArrayList;
import java.util.List;

public class MyQueue{

    private Object lock = new Object();
    private int maxSize;
    private List<String> list = new ArrayList<>(maxSize);

    public void MyQueue(int maxSize){
        this.maxSize = maxSize;
    }

    private void put(String s) {
        synchronized(lock) {
            try {
                if (list.size() == maxSize) {
                    System.out.println(Thread.currentThread().getName()+"---队列已满，正在等待...");
                    lock.wait();//等待释放锁
                }
                list.add(s);
                System.out.println(Thread.currentThread().getName()+"---入栈成功！");
                Thread.sleep(1000);
                lock.notifyAll();//唤醒释放锁

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void get(int i) {
        synchronized(lock) {
            try {
                if (list.size() == 0) {
                    System.out.println(Thread.currentThread().getName()+"---队列已空，正在等待...");
                    lock.wait();
                }
                list.remove(i);
                System.out.println(Thread.currentThread().getName()+"---出栈成功！");
                Thread.sleep(1000);
                lock.notifyAll();//唤醒释放锁
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        final MyQueue queue = new MyQueue();

        new Thread(() -> {
            queue.put("1");
            queue.put("2");
            queue.put("3");
            queue.put("4");
            queue.put("5");
            queue.put("6");
            queue.put("7");
        }).start();

        new Thread(() -> {
            queue.get(0);
            queue.get(0);
            queue.get(0);
            queue.get(0);
        }).start();
    }
}