package ru.otus.erinary.threads;

public class MyThread extends Thread {

    private Counter counter;
    private int lastSet;
    private int lastRead;
    private final Object monitor;

    public MyThread(Counter counter, Object monitor, int threadNumber) {
        this.counter = counter;
        this.lastSet = counter.getCount() - 1;
        this.lastRead = counter.getCount();
        this.monitor = monitor;
        this.setName("Thread" + threadNumber);
    }

    @Override
    public void run() {
        int num = 1;
        while (true) {
            synchronized (monitor) {
                if (lastRead != counter.getCount()) {
                    lastRead = counter.getCount();
                    System.out.println(currentThread().getName() + ": GET " + counter.getCount());
                }
                if (counter.getCount() == 10) {
                    break;
                }
                if (lastSet != counter.getCount()) {
                    counter.add(num);
                    lastSet = counter.getCount();
                    lastRead = counter.getCount();
                    System.out.println(currentThread().getName() + ": SET " + counter.getCount());
                }
            }
        }
    }

}
