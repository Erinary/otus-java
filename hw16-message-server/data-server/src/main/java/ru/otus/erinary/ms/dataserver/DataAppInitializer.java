package ru.otus.erinary.ms.dataserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@Slf4j
public class DataAppInitializer {

    public static void main(String[] args) {
        CommandLinePropertySource commandLine = new SimpleCommandLinePropertySource(args);
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.getEnvironment().getPropertySources().addFirst(commandLine);
        appContext.register(DataAppConfig.class);
        appContext.refresh();
        log.info("Data-server started");
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            appContext.close();
        }
    }

}
