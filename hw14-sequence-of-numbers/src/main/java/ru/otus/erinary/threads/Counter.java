package ru.otus.erinary.threads;

@SuppressWarnings("WeakerAccess")
public class Counter {
    private int startCount = 1;
    private int step = 1;

    public void tick() {
        startCount += step;
        enforceBorderConditions();
    }

    public int getCount() {
        return startCount;
    }

    private void enforceBorderConditions() {
        if (startCount == 10) {
            step = -1;
        } else if (startCount == 1) {
            step = 1;
        }
    }
}
