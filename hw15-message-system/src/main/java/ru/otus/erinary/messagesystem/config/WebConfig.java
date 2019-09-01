package ru.otus.erinary.messagesystem.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.messagesystem.controller.SocketHandler;
import ru.otus.erinary.messagesystem.service.DataBaseWorker;
import ru.otus.erinary.messagesystem.service.LocalMessageSystemClient;
import ru.otus.erinary.messagesystem.service.MessageSystemBroker;
import ru.otus.erinary.messagesystem.service.MessageSystemClient;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;
import ru.otus.erinary.orm.DBServiceImpl;
import ru.otus.erinary.orm.HibernateUtil;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableWebSocket
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer, WebSocketConfigurer {

    @Value("${datasource.url}")
    private String databaseUrl;
    @Value("${queue.frontend}")
    private String frontendQueueName;
    @Value("${queue.database}")
    private String dataBaseServiceQueueName;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @SuppressWarnings("SpringMVCViewInspection")
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(socketHandler(), "/ws");
    }

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtil.getSessionFactory(databaseUrl);
    }

    @Bean
    public H2DataBase h2DataBase() throws SQLException {
        return new H2DataBase(databaseUrl);
    }

    @Bean
    public DBService<User> userDBService() {
        return new DBServiceImpl<>(sessionFactory(), User.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(format);
        return mapper;
    }

    @Bean
    public MessageSystemBroker messageSystemService() {
        return new MessageSystemBroker(List.of(frontendQueueName, dataBaseServiceQueueName), 100);
    }

    @Bean
    public WebSocketHandler socketHandler() {
        SocketHandler socketHandler = SocketHandler.builder()
                .messageService(messageSystemClient())
                .objectMapper(objectMapper())
                .dataBaseServiceQueueName(dataBaseServiceQueueName)
                .build();
        messageSystemClient().registerListener(socketHandler, frontendQueueName);
        return socketHandler;
    }

    @Bean
    public DataBaseWorker dataBaseWorker() {
        DataBaseWorker dbWorker = DataBaseWorker.builder()
                .messageService(messageSystemClient())
                .dbService(userDBService())
                .frontendQueueName(frontendQueueName)
                .build();
        messageSystemClient().registerListener(dbWorker, dataBaseServiceQueueName);
        return dbWorker;
    }

    @Bean
    public MessageSystemClient messageSystemClient() {
        return new LocalMessageSystemClient(messageSystemService());
    }
}
