package ru.otus.erinary.diyarraylist;

import java.util.*;
import java.util.function.UnaryOperator;

public class DIYArrayList<T> implements List<T> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int CAPACITY_INCREASE_MULTIPLIER = 2;
    private T[] items;
    private int size;

    @SuppressWarnings("unchecked")
    public DIYArrayList() {
        this.items = (T[]) new Object[DEFAULT_CAPACITY];
    }

    @SuppressWarnings("unchecked")
    public DIYArrayList(int capacity) {
        this.items = (T[]) new Object[capacity];
    }

    /*---- Итераторы ----*/
    private class DIYIterator implements Iterator<T> {

        int current;
        int lastElem = -1;

        DIYIterator() {
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public T next() {
            T result;
            if (hasNext()) {
                result = get(current);
                lastElem = current;
                current += 1;
            } else {
                throw new NoSuchElementException();
            }
            return result;
        }

    }

    private class DIYListIterator extends DIYIterator implements ListIterator<T> {

        DIYListIterator(int index) {
            super();
            this.current = index;
        }

        @Override
        public boolean hasPrevious() {
            return current != 0;
        }

        @Override
        public T previous() {
            T result;
            if (hasPrevious()) {
                result = get(previousIndex());
                lastElem = previousIndex();
                current -= 1;
            } else {
                throw new NoSuchElementException();
            }
            return result;
        }

        @Override
        public int nextIndex() {
            return current + 1;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            DIYArrayList.this.set(lastElem, t);
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }

    }

    /*---- Методы ----*/
    private void increaseCapacity() {
        T[] old = items;
        items = Arrays.copyOf(old, old.length * CAPACITY_INCREASE_MULTIPLIER);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(items, items.length);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (size >= items.length) {
            increaseCapacity();
        }
        items[size] = t;
        ++size;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(items, 0, size, c);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; ++i) {
            items[i] = null;
        }
        size = 0;
    }

    @Override
    public T get(int index) {
        return items[Objects.checkIndex(index, items.length)];
    }

    @Override
    public T set(int index, T element) {
        T prev = items[Objects.checkIndex(index, items.length)];
        items[index] = element;
        return prev;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }

}
