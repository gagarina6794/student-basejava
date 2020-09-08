package com.urise.webapp;

public class DeadLock {
    public static Object LOCK1 = new Object();
    public static Object LOCK2 = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                runLock(LOCK1,LOCK2, Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                runLock(LOCK2, LOCK1, Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
    }

    private static void runLock(Object lock1, Object lock2, String threadID) throws InterruptedException {
        synchronized (lock1){
            System.out.println(threadID + " поток держит lock: " + lock1);
            System.out.println(threadID + " поток ждет lock: " + lock2);
            Thread.sleep(1000);
            synchronized (lock2){
                System.out.println(threadID + " поток держит lock: " + lock2);
            }
        }
    }
}
