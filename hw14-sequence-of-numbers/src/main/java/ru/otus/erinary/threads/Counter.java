package ru.otus.erinary.threads;

@SuppressWarnings("WeakerAccess")
public class Counter {
    private int count;
    private int step;
    private int target;

    public Counter(int count) {
        this.count = count;
        this.step = 1;
        this.target = 10;
    }

    public void tick() {
        count += step;
    }

    public int getCount() {
        return count;
    }

    public void enforceBorderConditions() {
        if (count == 10) {
            step = -1;
            target = 1;
        } else if (count == 1) {
            step = 1;
            target = 10;
        }
    }
}
