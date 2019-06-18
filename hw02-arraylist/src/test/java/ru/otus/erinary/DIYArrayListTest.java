package ru.otus.erinary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DIYArrayListTest {

    private static final int TEST_SIZE = 100;
    private DIYArrayList<Integer> testList = new DIYArrayList<>(TEST_SIZE);

    @Test
    void testAddAll() {
        Integer[] elements = formTestData();
        Collections.addAll(testList, elements);
        assertEquals(elements.length, testList.size());
        for (int i = 0; i < elements.length; i++) {
            assertEquals(elements[i], testList.get(i));
        }
    }

    @Test
    void testCopy() {
        Integer[] elements = formTestData();
        List<Integer> expected = new ArrayList<>();

        Collections.addAll(testList, elements);
        Collections.addAll(expected, elements);

        List<Integer> src = List.of(1, 2, 3, 4, 5);
        Collections.copy(testList, src);
        Collections.copy(expected, src);

        assertEquals(expected.size(), testList.size());
        for (int i = 0; i < expected.size(); ++i) {
            assertEquals(expected.get(i), testList.get(i));
        }
    }

    @Test
    void testSort() {
        Integer[] elements = formRandomTestData();
        Collections.addAll(testList, elements);
        Collections.sort(testList, Comparator.comparingInt(o -> o));
        assertTrue(isSorted(testList, Comparator.comparingInt(o -> o)));
    }

    @AfterEach
    void clearTestList() {
        testList.clear();
    }

    private Integer[] formTestData() {
        Integer[] result = new Integer[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; ++i) {
            result[i] = i;
        }
        return result;
    }

    private Integer[] formRandomTestData() {
        Random random = new Random();
        Integer[] result = new Integer[TEST_SIZE];
        for (int i = 0; i < TEST_SIZE; ++i) {
            result[i] = random.ints(-100, 100).limit(1).findFirst().getAsInt();
        }
        return result;
    }

    private <T> boolean isSorted(List<T> testList, Comparator<T> cmp) {
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