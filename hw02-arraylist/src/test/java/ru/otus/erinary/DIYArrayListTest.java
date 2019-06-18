package ru.otus.erinary;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DIYArrayListTest {

    private static final int TEST_SIZE = 20;
    private DIYArrayList<Integer> testList = new DIYArrayList<>(TEST_SIZE);

    @Test
    void testAddAll() {
        Collections.addAll(testList, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        assertFalse(testList.isEmpty());
        assertEquals(20, testList.size());
    }

    @Test
    void testCopy() {
        List<Integer> src = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        Collections.copy(testList, src);
        assertFalse(testList.isEmpty());
        assertEquals(20, testList.size());
    }

    @Test
    void testSort() {
        Collections.addAll(testList, 1,42,13,54,12,34,122,54,65,134,7,53,86,4,81,9,10,1,44,90);
        Collections.sort(testList, (o1, o2) -> o1 - o2);
        assertTrue(isSorted(testList, (o1, o2) -> o1 - o2));
    }

    @AfterEach
    void clearTestList() {
        testList.clear();
    }

    private <T> boolean isSorted(List<T> testList, Comparator<T> cmp) {
        T prev = null;
        for (T elem: testList) {
            if (prev != null && cmp.compare(prev, elem) > 0) {
                return false;
            }
            prev = elem;
        }
        return true;
    }
}