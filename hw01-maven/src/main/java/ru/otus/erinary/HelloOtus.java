package ru.otus.erinary;

import com.google.common.base.Joiner;

import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> list = List.of("a", "b", "c", "d", "e");
        System.out.println(Joiner.on(",").skipNulls().join(list));
    }
}
