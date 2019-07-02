package ru.otus.erinary.diyarraylist;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static ru.otus.erinary.diyarraylist.DIYArrayListTest.TEST_SIZE;

class TestHelper {

    static Integer[] formTestData() {
        Integer[] result = new Integer[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; ++i) {
            result[i] = i;
        }
        return result;
    }

    static Integer[] formRandomTestData() {
        Random random = new Random();
        Integer[] result = new Integer[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; ++i) {
            result[i] = random.ints(-100, 100).limit(1).findFirst().getAsInt();
        }
        return result;
    }

    static <T> boolean isSorted(List<T> testList, Comparator<T> cmp) {
        T prev = null;
        for (T elem : testList) {
            if (prev != null && cmp.compare(prev, elem) > 0) {
                return false;
            }
            prev = elem;
        }
        return true;
    }
}
