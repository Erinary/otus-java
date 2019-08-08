package ru.otus.erinary.threads;

public class MyThread extends Thread {

    private Counter counter;
    private int lastSet;
    private int lastRead;
    private final Object monitor;

    @SuppressWarnings("WeakerAccess")
    public MyThread(Counter counter, Object monitor, int threadNumber) {
        this.counter = counter;
        this.lastSet = counter.getCount() - 1;
        this.lastRead = counter.getCount();
        this.monitor = monitor;
        this.setName("Thread" + threadNumber);
    }

    @Override
    public void run() {
        while (true) {
            sleep();
            synchronized (monitor) {
                if (lastRead != counter.getCount()) {
                    lastRead = counter.getCount();
                    System.out.println(currentThread().getName() + ": GET " + counter.getCount());
                }
                if (lastSet != counter.getCount()) {
                    counter.tick();
                    counter.enforceBorderConditions();
                    lastSet = counter.getCount();
                    lastRead = counter.getCount();
                    System.out.println(currentThread().getName() + ": SET " + counter.getCount());
                }
            }
        }
    }

    private void sleep() {
        try {
            sleep(500);
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

}
