package com.urise.webapp;

public class DeadLock {
    public static Object LOCK1 = new Object();
    public static Object LOCK2 = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK1){
                System.out.println("1 поток держит lock1");
                System.out.println("1 поток ждет lock2");
                synchronized (LOCK2){
                    System.out.println("1 поток держит lock2");
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (LOCK2){
                System.out.println("2 поток держит lock2");
                System.out.println("2 поток ждет lock1");
                synchronized (LOCK1){
                    System.out.println("2 поток держит lock1");
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("завершение выполнения потоков");
    }
}
