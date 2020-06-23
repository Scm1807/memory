package multi.multi_003.coll013;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class UseQueue {

    public static void main(String[] args) throws Exception {

        //高性能无阻塞无界队列：ConcurrentLinkedQueue

        /**
         //高性能无阻塞无界队列
         ConcurrentLinkedQueue<String> q = new ConcurrentLinkedQueue<String>();
         q.offer("a");
         q.offer("b");
         q.offer("c");
         q.offer("d");
         q.add("e");

         System.out.println(q.poll());	//a 从头部取出元素，并从队列里删除
         System.out.println(q.size());	//4
         System.out.println(q.peek());	//b 拿到元素，但不删除
         System.out.println(q.size());	//4
         */

        /**
         //有界队列
         ArrayBlockingQueue<String> array = new ArrayBlockingQueue<String>(5);
         array.put("a");
         array.put("b");
         array.add("c");
         array.add("d");
         array.add("e");
         array.add("f");
         // 3秒钟如果放不进去返回失败
         //System.out.println(array.offer("a", 3, TimeUnit.SECONDS));
         */


        ///**
        //阻塞队列， 无界队列
        LinkedBlockingQueue<String> q = new LinkedBlockingQueue<>();
        q.offer("a");
        q.offer("b");
        q.offer("c");
        q.offer("d");
        q.offer("e");
        q.add("f");
        q.add("g");

        for (Iterator iterator = q.iterator(); iterator.hasNext(); ) {
            String string = (String) iterator.next();
            System.out.println(string);
        }
        System.out.println("队列大小：" + q.size());

        List<String> list = new ArrayList<>();
        // 一次性从BlockingQueue获取所有可用的数据对象（还可以指定获取数据的个数）
        //通过该方法，可以提升获取数据效率；不需要多次分批加锁或释放锁。
        System.out.println("drainTo元素个数：" + q.drainTo(list, 3));
        System.out.println("list大小：" + list.size());
        for (String string : list) {
            System.out.println(string);
        }
        System.out.println("drainTo后元素个数：" + q.size());
        //*/


        /**
         //SynchronousQueue 没有存储元素的功能
         final SynchronousQueue<String> q = new SynchronousQueue<>();
         Thread t1 = new Thread(() -> {
         try {
         System.out.println("进入阻塞...");
         // 进入阻塞但是处于阻塞，直到线程2添加元素后可以拿到元素
         System.out.println("消费：" + q.take());
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         });
         t1.start();
         Thread.sleep(4000);
         // 只调用add方法会报错，需要提前调用take方法，take阻塞
         Thread t2 = new Thread(() -> q.add("asdasd"));
         t2.start();
         */
    }
}
