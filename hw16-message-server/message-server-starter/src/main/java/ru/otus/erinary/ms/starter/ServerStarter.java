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
            List<String> messageServerArgs = List.of(
                    "--port=8888",
                    "--queues=to-web-service,to-second-web-service,to-data-service"
            );
            Process messageServer = exec(MessageServerStarter.class.getName(), messageServerArgs).inheritIO().start();

            while (!messageServer.isAlive()) {
                Thread.sleep(500);
            }

            log.info("Starting first web-server");
            List<String> webServerArgs = List.of(
                    "--server.port=8085",
                    "--ms.listened.queue=to-web-service"
            );
            Process webServer = exec(WebAppInitializer.class.getName(), webServerArgs).inheritIO().start();

            log.info("Starting second web-server");
            List<String> secondWebServerArgs = List.of(
                    "--server.port=8090",
                    "--ms.listened.queue=to-second-web-service"
            );
            Process secondWebServer = exec(WebAppInitializer.class.getName(), secondWebServerArgs).inheritIO().start();

            log.info("Starting first data-server");
            List<String> dataServerArgs = List.of(
                    "--datasource.url=jdbc:h2:./hw16-message-server/data-server/h2-database/user_base;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE",
                    "--data.server.name=first-data-server"
            );
            Process dataServer = exec(DataAppInitializer.class.getName(), dataServerArgs).inheritIO().start();

            log.info("Starting second data-server");
            List<String> secondDataServerArgs = List.of(
                    "--datasource.url=jdbc:h2:./hw16-message-server/data-server/h2-database/user_base;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE",
                    "--data.server.name=second-data-server"
            );
            Process secondDataServer = exec(DataAppInitializer.class.getName(), secondDataServerArgs).inheritIO().start();

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
