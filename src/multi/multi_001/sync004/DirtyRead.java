package multi.multi_001.sync004;

/**
 * 业务整体需要使用完整的synchronized，保持业务的原子性。
 *
 * @author alienware
 */
public class DirtyRead {

    private String username = "bjsxt";
    private String password = "123";

    public synchronized void setValue(String username, String password) {
        this.username = username;

        try {
            System.out.println(Thread.currentThread().getName() + ":睡3秒后改密码");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.password = password;

        System.out.println("setValue最终结果：username = " + username + " , password = " + password);
    }

    public void getValue() {
        System.out.println("getValue方法得到：username = " + this.username + " , password = " + this.password);
    }

    public static void main(String[] args) throws Exception {

        final DirtyRead dr = new DirtyRead();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                dr.setValue("z3", "456");
            }
        });
        t1.start();

        System.out.println(Thread.currentThread().getName() + ":睡三秒");
        Thread.sleep(3000);
        dr.getValue();
    }

}
