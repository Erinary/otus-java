package ru.otus.erinary.gc;

import java.util.ArrayList;
import java.util.List;

/*
* JVM params:
* -Xms512m
* -Xmx512m
* -Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
* -XX:+HeapDumpOnOutOfMemoryError
* -XX:HeapDumpPath=./logs/dump
*
* GC:
* -XX:+UseSerialGC
* -XX:+UseParallelGC
* -XX:+UseG1GC
*/

public class MemoryLeak {

    private static String longString = "This string soooo long!";
    private static final List<Object> list = new ArrayList<>(100);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            list.add(longString);
            longString = longString + " -very";
            if (i % 10 == 0) {
                list.remove(0);
                Thread.sleep(120);
            }
            System.out.println("List size: " + list.size());
        }
    }
}
