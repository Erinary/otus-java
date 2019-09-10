package ru.otus.erinary.ms.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.erinary.ms.dataserver.DataAppInitializer;
import ru.otus.erinary.ms.messageserver.MessageServerStarter;
import ru.otus.erinary.ms.webserver.WebAppInitializer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerStarter {

    private static final Logger log = LoggerFactory.getLogger(ServerStarter.class);
    private static final String JAVA_BIN = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    private static final String CLASSPATH = System.getProperty("java.class.path");

    public static void main(String[] args) {
        try {
            log.info("Starting message server");
            Process messageServer = exec(MessageServerStarter.class.getName(), new ArrayList<>()).inheritIO().start();

            while (!messageServer.isAlive()) {
                Thread.sleep(500);
            }

            log.info("Starting web-server");
            Process webServer = exec(WebAppInitializer.class.getName(), new ArrayList<>()).inheritIO().start();
            log.info("Starting data-server");
            Process dataServer = exec(DataAppInitializer.class.getName(), new ArrayList<>()).inheritIO().start();
            //noinspection InfiniteLoopStatement
            while (true) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    private static ProcessBuilder exec(String className, List<String> args) {
        List<String> command = new ArrayList<>();
        command.add(JAVA_BIN);
        command.add("-cp");
        command.add(CLASSPATH);
        command.add(className);
        command.addAll(args);

        return new ProcessBuilder(command);
    }

}
