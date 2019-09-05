package ru.otus.erinary.ms.dataserver;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.erinary.ms.dataserver.service.DataServer;

public class AppInitializer {

    public static void main(String[] args) {
        ApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        DataServer dataServer = appContext.getBean(DataServer.class);
        dataServer.start();
    }

}
