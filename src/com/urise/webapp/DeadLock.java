package com.urise.webapp;

public class DeadLock {
    public static Object LOCK1 = new Object();
    public static Object LOCK2 = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> runLock(LOCK1, LOCK2, Thread.currentThread().getName())).start();
       new Thread(() -> runLock(LOCK2, LOCK1, Thread.currentThread().getName())).start();
    }

    private static void runLock(Object lock1, Object lock2, String threadID) {
        synchronized (lock1) {
            System.out.println(threadID + " поток держит lock: " + lock1);
            System.out.println(threadID + " поток ждет lock: " + lock2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println(threadID + " поток держит lock: " + lock2);
            }
        }
    }
}
