package ru.otus.erinary.threads;

import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        new Main().start();
    }

    private void start() throws InterruptedException {
        Counter counter = new Counter();
        List<MyThread> threadList = List.of(
                new MyThread(counter, "thread1"),
                new MyThread(counter, "thread2")
        );

        threadList.forEach(Thread::start);

        for (int i = 0; i < 20; i++) {
            for (MyThread t : threadList) {
                t.getValueChangedMonitor().kick();
                t.getValueReadDoneMonitor().waitKicked();
            }
            counter.tick();
        }

        threadList.forEach(Thread::interrupt);
        for (Thread thread : threadList) {
            thread.join();
        }
    }

}
