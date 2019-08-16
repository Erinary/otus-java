package ru.otus.erinary.spring;

import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;
import ru.otus.erinary.orm.DBServiceImpl;
import ru.otus.erinary.orm.HibernateUtil;

import java.sql.SQLException;

@Configuration
@EnableWebMvc
@ComponentScan
@PropertySource("classpath:application.properties")
public class WebConfig {

    @Autowired
    private Environment env;

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory(env.getProperty("datasource.url"));
    }

    @Bean
    public H2DataBase h2DataBase() throws SQLException {
        return new H2DataBase(env.getProperty("datasource.url"));
    }

    @Bean
    public DBService<User> userDBService() {
        return new DBServiceImpl<>(sessionFactory(), User.class);
    }

    @Bean
    public Gson gson() {
        return new Gson();
    }
}
