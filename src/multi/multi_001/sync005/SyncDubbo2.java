package multi.multi_001.sync005;

/**
 * synchronized的重入
 *
 * @author alienware
 */
public class SyncDubbo2 {

    static class Main {
        public int i = 10;

        public synchronized void operationMain() {
            try {
                i--;
                System.out.println("Main print i = " + i);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Sub extends Main {
        public synchronized void operationSub() {
            try {
                while (i > 0) {
                    i--;
                    System.out.println("Sub print i = " + i);
                    Thread.sleep(100);
                    this.operationMain();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Sub sub = new Sub();
                sub.operationSub();
            }
        });

        t1.start();
    }

}
