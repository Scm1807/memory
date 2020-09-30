package algorithm;

public class ThreadTest{


    public static void main(String[] args) {

        Depot depot = new Depot(10);
        Producer producer = new Producer(depot);
        Consumer consumer = new Consumer(depot);

        consumer.consum(5);
        //producer.pruduce(10);
        consumer.consum(10);
    }
}
//定义仓库类
class Depot {

    private int size;//仓库现有的商品数量
    private int capacity;//仓库容量

    Depot(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void consume(int number) {
        try {
            //大于0开始消费
            while (number > 0) {
                while (size <= 0) {
                    System.out.println("商品缺货，正在生产，请等待...");
                    wait();
                }
                //判断消费数量是否大于仓库库存 ,取
                int deleteNumber = size < number ? size : number;
                size -= deleteNumber;
                System.out.println("消费者希望消费： " + number + "  \n实际消费 : " + deleteNumber + "  \n仓库剩余： " + size);
                //消费结束，唤醒其他线程的消费或者生产
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void produce(int number) {
        try {
            while (number > 0) { //需要生产number 大于零
                //如果生产出来的东西太多就等待
                while (size >= capacity) {
                    wait();
                }
                int addNumber = (number <= (capacity - size)) ? number : (capacity - size);
                System.out.println("实际生产数量 ： " + addNumber + " , \n仓库库存为 ： " + (size + addNumber));
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Producer {
    private Depot depot;

    Producer(Depot depot) {
        this.depot = depot;
    }

    public void pruduce(int number) {
        new Thread(() -> depot.produce(number)).start();
    }
}

class Consumer {
    private Depot depot;

    Consumer(Depot depot) {
        this.depot = depot;
    }

    public void consum(int number){
        new Thread(()-> depot.consume(number)).start();
    }
}

