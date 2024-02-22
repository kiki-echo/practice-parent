package org.example.demo;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    public static void main(String[] args) {
        ExecutorService pool= Executors.newFixedThreadPool(1);
        pool.submit(new MyRunnable());
        pool.submit(new MyRunnable());
        pool.submit(new MyRunnable());
        pool.submit(new MyRunnable());
    }






    public static class  MyCallable implements Callable<String> {

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
