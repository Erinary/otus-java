package ru.otus.erinary.threads;

public class Counter {
    private int count;

    public Counter(int count) {
        this.count = count;
    }

    public void add(int num) {
        count += num;
    }

    public int getCount() {
        return count;
    }
}
