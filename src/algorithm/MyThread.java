package algorithm;

public class MyThread extends Thread {

    private static int times = 1;//运行几次
    private static final int total = 50;//总共的输出数量
    private static final int length = 5;//每个线程输出的数量

    public static void main(String[] args) {

        while (times++ <= (length / total)) {
            new Thread(() -> {  //lambda 表达式实现run方法
                for (int i = (times / 5 + 1); i < times + 5; i++) {
                    if (times / 5 % 2 == 0) {
                        System.out.println("Thread_1_" + i);
                    }
                    System.out.println(Thread.currentThread().getName() + "-" + i);
                }
            }).start();
            new Thread(() -> {  //lambda 表达式实现run方法
                for (int i = (times / 5 + 1); i < times + 5; i++) {
                    if (times / 5 % 2 == 0) {
                        System.out.println("Thread_1_" + i);
                    }
                    System.out.println(Thread.currentThread().getName() + "-" + i);
                }
            }).start();
        }
        times++;
    }
}
class Count extends Thread{
    private int begin;
    private int end;

    public Count() {
        super("Thread_1_");
    }

    private void set(int begin,int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {

    }
}