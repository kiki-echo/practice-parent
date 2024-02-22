package org.example.demo;

public class ThreadDead {
    public static Object o1=new Object();
    public static Object o2=new Object();
    public static void main(String[] args) {
       new Thread(new Runnable() {
           @Override
           public void run() {
               synchronized (o1){
                   System.out.println(Thread.currentThread().getName()+"占用资源o1");
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       throw new RuntimeException(e);
                   }
                   synchronized (o2){
                       System.out.println(Thread.currentThread().getName()+"占用资源o2");
                   }
               }

           }
       },"线程1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (o2){
                    System.out.println(Thread.currentThread().getName()+"占用资源o2");
                    synchronized (o1){
                        System.out.println(Thread.currentThread().getName()+"占用资源o1");
                    }
                }

            }
        },"线程2").start();
    }
}
