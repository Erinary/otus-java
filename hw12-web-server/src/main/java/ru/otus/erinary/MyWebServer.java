package ru.otus.erinary;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.erinary.dbutils.DataBaseContext;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBServiceImpl;
import ru.otus.erinary.servlet.UserControllerServlet;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;

@SuppressWarnings("WeakerAccess")
@Slf4j
public class MyWebServer {

    private final DataBaseContext dataBaseContext;
    private static final int PORT = 8085;

    public MyWebServer() throws SQLException {
        this.dataBaseContext = new DataBaseContext();
    }

    public void start() throws Exception {
        Server server = createServer();
        server.start();
        server.join();
        log.error("MyWebServer Started");
    }

    private Server createServer() {
        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler();

        context.setBaseResource(
                Resource.newResource(MyWebApp.class.getClassLoader().getResource("web"))
        );
        context.addServlet(new ServletHolder("default", DefaultServlet.class), "/");
        context.addServlet(new ServletHolder(createUserControllerServlet()), "/users");
        context.setSecurityHandler(createSecurityHandler());

        server.setHandler(context);

        return server;
    }

    private UserControllerServlet createUserControllerServlet() {
        return new UserControllerServlet(new DBServiceImpl<>(dataBaseContext.getSessionFactory(), User.class));
    }

    private SecurityHandler createSecurityHandler() {
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{"admin"});

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler handler = new ConstraintSecurityHandler();
        handler.setAuthenticator(new BasicAuthenticator());

        URL properties = MyWebApp.class.getClassLoader().getResource("realm.properties");
        if (properties == null) {
            throw new RuntimeException("realm.properties not found");
        }
        handler.setLoginService(new HashLoginService("Realm", properties.getPath()));
        handler.setConstraintMappings(Collections.singletonList(mapping));
        return handler;
    }

    public void shutdown() throws SQLException {
        dataBaseContext.cleanup();
    }
}
