package ru.otus.erinary.ms.dataserver;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DataAppInitializer {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(DataAppConfig.class);
        try {
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            appContext.close();
        }
    }

}
