package ru.otus.erinary.threads;

import lombok.Getter;

public class MyThread extends Thread {

    @Getter
    private final KickMonitor valueChangedMonitor;
    @Getter
    private final KickMonitor valueReadDoneMonitor;
    private Counter counter;

    @SuppressWarnings("WeakerAccess")
    public MyThread(Counter counter, String name) {
        this.valueChangedMonitor = new KickMonitor();
        this.valueReadDoneMonitor = new KickMonitor();
        this.counter = counter;
        this.setName(name);
    }

    @Override
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                valueChangedMonitor.waitKicked();
                System.out.println(String.format("[%s] %d", this.getName(), counter.getCount()));
                valueReadDoneMonitor.kick();
            }
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }
}
