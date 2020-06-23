package kthis;

import java.util.ArrayList;
import java.util.List;

//已上传
public class MyQueue {

    private final Object lock = new Object();
    private int maxSize;// 队列总长度
    private List<String> list = new ArrayList<>(maxSize);

    private MyQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    private void put(String person) {
        synchronized (lock) {
            try {
                if (list.size() == maxSize) {
                    System.out.println(Thread.currentThread().getName() + ":       队列已满，正在等待...\n");
                    lock.wait(); // 等待释放锁
                }
                list.add(person);
                System.out.println(Thread.currentThread().getName() + ":    " + person + "--" + "入栈成功！\n");
                lock.notifyAll(); // 唤醒释放锁

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void get() {
        synchronized (lock) {
            try {
                if (list.size() == 0) {
                    System.out.println(Thread.currentThread().getName() + ":       队列已空，正在等待...\n");
                    lock.wait();
                }
                String person = list.remove(0);
                System.out.println(Thread.currentThread().getName() + ":    " + person + "--" + "出栈成功！\n");
                Thread.sleep(1000);
                lock.notifyAll(); // 唤醒释放锁
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final MyQueue queue = new MyQueue(5);

        new Thread(() -> {
            int count = 0;
            while (true) {
                queue.put(count++ + "号");
            }
        }).start();

        new Thread(() -> {
            while (true) {
                queue.get();
            }
        }).start();
    }

}