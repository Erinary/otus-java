package ru.otus.erinary;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.erinary.dbutils.DataBaseUtils;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBServiceImpl;
import ru.otus.erinary.servlet.UserControllerServlet;

import java.sql.SQLException;

public class MyWebApp {

    private final DataBaseUtils dataBaseUtils;
    private final int PORT = 8085;

    private MyWebApp() throws SQLException {
        this.dataBaseUtils = new DataBaseUtils();
    }

    public static void main(String[] args) {
        try {
            MyWebApp app = new MyWebApp();
            try {
                app.start();
            } finally {
                app.shutdown();
            }
        } catch (Exception e) {
            System.out.println("Application initialization error");
            System.exit(-1);
        }
    }

    private void start() throws Exception {
        Server server = createServer();
        server.start();
        server.join();
    }

    private Server createServer() {
        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(new ServletHolder(prepareUserControllerServlet()), "/home/users");
        server.setHandler(context);
        return server;
    }

    private UserControllerServlet prepareUserControllerServlet() {
        return new UserControllerServlet(new DBServiceImpl<>(dataBaseUtils.getSessionFactory(), User.class));
    }

    private void shutdown() throws SQLException {
        dataBaseUtils.cleanup();
    }
}
