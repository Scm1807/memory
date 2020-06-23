package multi.multi_003.coll013;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * 基于优先级的阻塞队列，通过add添加的对象的compareble方法决定取元素的顺序
 */
public class UsePriorityBlockingQueue {

    public static void main(String[] args) throws Exception {
        // Task 必须实现 Compareble接口，用来比较id的大小
        PriorityBlockingQueue<Task> q = new PriorityBlockingQueue<>();
        Task t1 = new Task();
        t1.setId(3);
        t1.setName("id为3");

        Task t2 = new Task();
        t2.setId(4);
        t2.setName("id为4");

        Task t3 = new Task();
        t3.setId(1);
        t3.setName("id为1");

        //添加时不排序
        q.add(t1);    //3
        q.add(t2);    //4
        q.add(t3);    //1

        // 1 3 4 take的时候再排序
        System.out.println("容器：" + q);
        System.out.println(q.take().getId());
        System.out.println("容器：" + q);
        System.out.println(q.take().getId());
        System.out.println("容器：" + q);
        System.out.println(q.take().getId());
        System.out.println("容器：" + q);
    }
}
