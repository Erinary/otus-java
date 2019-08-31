package ru.otus.erinary;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyWebApp {

    public static void main(String[] args) {
        try {
            MyWebServer server = new MyWebServer();
            try {
                server.start();
            } finally {
                server.shutdown();
            }
        } catch (Exception e) {
            log.error("Application initialization error: {}", e.getMessage());
            System.exit(-1);
        }
    }

}
