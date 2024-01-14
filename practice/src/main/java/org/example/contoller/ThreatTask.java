package org.example.contoller;

import java.util.concurrent.*;

public class ThreatTask {
    public static void main(String[] args) {
        Thread t1=new MyThreat();
        t1.setName("子线程1");
        t1.start();

        Thread t2 = new Thread(new MyRunnable(),"子线程2");
        t2.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(Thread.currentThread().getName()+"- >" +i);
                }
            }
        },"子线程3").start();


        Callable callable= new MyCallable();
        FutureTask futureTask=new FutureTask<>(callable);
        Thread t4 =new Thread(futureTask,"子线程4");
        t4.start();

        int sum =0;
        for (int i = 0; i < 100; i++) {
            sum+=i;
            System.out.println(Thread.currentThread().getName()+"- >" +i);
        }
        try {
            System.out.println(futureTask.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+"- >" +sum);



    }


public static class  MyCallable implements Callable<String>{

    @Override
    public String call() throws Exception {
int sum =0;
        for (int i = 0; i < 100; i++) {
            sum+=i;
            System.out.println(Thread.currentThread().getName()+"- >" +i);
        }
        return Thread.currentThread().getName()+"- >" +sum;
    }
}




    public  static  class  MyRunnable implements  Runnable{

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName()+"- >" +i);
            }
        }
    }


    public static class  MyThreat extends  Thread{
        @Override
public  void  run(){
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName()+"- >" +i);
            }
        }
    }
}
