package com.ly.test;

/**
 * Created by Administrator on 2015/3/20.
 * 生产者与消费者模型中，要保证以下几点：
 * 1 同一时间内只能有一个生产者生产     生产方法加锁sychronized
 * 2 同一时间内只能有一个消费者消费     消费方法加锁sychronized
 * 3 生产者生产的同时消费者不能消费     生产方法加锁sychronized
 * 4 消费者消费的同时生产者不能生产     消费方法加锁sychronized
 * 5 共享空间空时消费者不能继续消费     消费前循环判断是否为空，空的话将该线程wait，释放锁允许其他同步方法执行
 * 6 共享空间满时生产者不能继续生产     生产前循环判断是否为满，满的话将该线程wait，释放锁允许其他同步方法执行
 *
*/
public class ProducerConsumer {
    public static void main(String[] args)
    {
        StackBasket s = new StackBasket();
        Producer p = new Producer(s);
        Consumer c = new Consumer(s);
        Thread tp = new Thread(p);
        Thread tc = new Thread(c);
        tp.start();
        tc.start();
    }

}

//
class Mantou
{
    private int id;

    Mantou(int id){
        this.id = id;
    }

    public String toString(){
        return "Mantou :" + id;
    }
}

//共享栈空间
class StackBasket
{
    Mantou sm[] = new Mantou[6];
    int index = 0;

    /**
     * show 生产方法.
     * show 该方法为同步方法，持有方法锁；
     * show 首先循环判断满否，满的话使该线程等待，释放同步方法锁，允许消费；
     * show 当不满时首先唤醒正在等待的消费方法，但是也只能让其进入就绪状态，
     * show 等生产结束释放同步方法锁后消费才能持有该锁进行消费
     * @param m 元素
     * @return 没有返回值
     */

    public synchronized void push(Mantou m){
        try{
            while(index == sm.length){
                System.out.println("!!!!!!!!!生产满了!!!!!!!!!");
                this.wait();
            }
            this.notify();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(IllegalMonitorStateException e){
            e.printStackTrace();
        }

        sm[index] = m;
        index++;
        System.out.println("生产了：" + m + " 共" + index + "个馒头");
    }

    /**
     * show 消费方法
     * show 该方法为同步方法，持有方法锁
     * show 首先循环判断空否，空的话使该线程等待，释放同步方法锁，允许生产；
     * show 当不空时首先唤醒正在等待的生产方法，但是也只能让其进入就绪状态
     * show 等消费结束释放同步方法锁后生产才能持有该锁进行生产
     * @param b true 表示显示，false 表示隐藏
     * @return 没有返回值
     */
    public synchronized Mantou pop(){
        try{
            while(index == 0){
                System.out.println("!!!!!!!!!消费光了!!!!!!!!!");
                this.wait();
            }
            this.notify();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(IllegalMonitorStateException e){
            e.printStackTrace();
        }
        index--;
        System.out.println("消费了：---------" + sm[index] + " 共" + index + "个馒头");
        return sm[index];
    }
}

class Producer implements Runnable
{
    StackBasket ss = new StackBasket();
    Producer(StackBasket ss){
        this.ss = ss;
    }

    /**
     * show 生产进程.
     */
    public void run(){
        for(int i = 0;i < 20;i++){
            Mantou m = new Mantou(i);
            ss.push(m);
//          System.out.println("生产了：" + m + " 共" + ss.index + "个馒头");
//          在上面一行进行测试是不妥的，对index的访问应该在原子操作里，因为可能在push之后此输出之前又消费了，会产生输出混乱
            try{
                Thread.sleep((int)(Math.random()*500));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable
{
    StackBasket ss = new StackBasket();
    Consumer(StackBasket ss){
        this.ss = ss;
    }

    /**
     * show 消费进程.
     */
    public void run(){
        for(int i = 0;i < 20;i++){
            Mantou m = ss.pop();
//          System.out.println("消费了：---------" + m + " 共" + ss.index + "个馒头");
//  同上  在上面一行进行测试也是不妥的，对index的访问应该在原子操作里，因为可能在pop之后此输出之前又生产了，会产生输出混乱
            try{
                Thread.sleep((int)(Math.random()*1000));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}