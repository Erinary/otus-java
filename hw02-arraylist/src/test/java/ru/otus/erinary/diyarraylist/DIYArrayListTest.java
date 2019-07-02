package ru.otus.erinary.diyarraylist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.erinary.diyarraylist.TestHelper.*;

class DIYArrayListTest {

    static final int TEST_SIZE = 100;
    private DIYArrayList<Integer> testList;

    @BeforeEach
    void setup() {
        testList = new DIYArrayList<>(TEST_SIZE);
    }

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
}