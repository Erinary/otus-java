package ru.otus.erinary.ms.dataserver;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.otus.erinary.ms.dataserver.dao.AddressModel;
import ru.otus.erinary.ms.dataserver.dao.PhoneModel;
import ru.otus.erinary.ms.dataserver.dao.UserModel;
import ru.otus.erinary.ms.dataserver.h2.H2DataBase;
import ru.otus.erinary.ms.dataserver.orm.DBService;
import ru.otus.erinary.ms.dataserver.orm.DBServiceImpl;
import ru.otus.erinary.ms.dataserver.service.DataService;
import ru.otus.erinary.ms.dataserver.service.SpringSocketClient;
import ru.otus.erinary.ms.messageserver.service.SocketClient;

import java.sql.SQLException;
import java.util.Properties;

@ComponentScan
@Configuration
@PropertySource("classpath:data-application.properties")
public class DataAppConfig {

    private final Environment env;

    public DataAppConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SessionFactory sessionFactory() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(hibernateProperties())
                .build();
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(UserModel.class);
        metadataSources.addAnnotatedClass(AddressModel.class);
        metadataSources.addAnnotatedClass(PhoneModel.class);
        return metadataSources.getMetadataBuilder().build().getSessionFactoryBuilder().build();
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.url", env.getRequiredProperty("datasource.url"));
        properties.put("connection.driver_class", env.getRequiredProperty("connection.driver_class"));
        properties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        properties.put("format_sql", env.getRequiredProperty("format_sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean
    public H2DataBase h2DataBase() throws SQLException {
        return new H2DataBase(env.getRequiredProperty("datasource.url"));
    }

    @Bean
    public DBService<UserModel> userDBService() {
        return new DBServiceImpl<>(sessionFactory(), UserModel.class);
    }

    @Bean
    public DataService dataServer() {
        SocketClient client = clientSocket();
        DataService dataService = new DataService(userDBService(), client);
        dataService.setServerName(env.getRequiredProperty("data.server.name"));
        client.registerListener(dataService);
        return dataService;
    }

    @Bean
    public SocketClient clientSocket() {
        return new SpringSocketClient(
                env.getRequiredProperty("listened.queue"),
                Integer.parseInt(env.getRequiredProperty("server.port")),
                env.getRequiredProperty("server.host")
        );
    }

}
