package multi.multi_001.sync006;

/**
 * 锁对象的改变问题
 *
 * @author alienware
 */
public class ChangeLock {

    private String lock = "lock";

    private void method() {
        synchronized (lock) {
            try {
                System.out.println("当前线程 : " + Thread.currentThread().getName() + "开始");
                lock = "change lock"; // lock对象改变不会有影响
                Thread.sleep(4000);
                System.out.println("当前线程 : " + Thread.currentThread().getName() + "结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final ChangeLock changeLock = new ChangeLock();
        Thread t1 = new Thread(() -> changeLock.method(), "t1");
        Thread t2 = new Thread(() -> changeLock.method(), "t2");
        t1.start();
        t2.start();
    }

}
