package ru.otus.erinary.threads;

public class Main {

    private final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Main().start();
    }

    private void start() throws InterruptedException {
        Counter counter = new Counter(0);
        Thread t1 = new MyThread(counter, monitor, 1);
        Thread t2 = new MyThread(counter, monitor, 2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

}
