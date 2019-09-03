package ru.otus.erinary.threads;

@SuppressWarnings("WeakerAccess")
public class KickMonitor {

    private boolean wasKicked = false;

    public synchronized void waitKicked() throws InterruptedException {
        while (!wasKicked) {
            this.wait();
        }
        wasKicked = false;
    }

    public synchronized void kick() {
        wasKicked = true;
        this.notifyAll();
    }

}
