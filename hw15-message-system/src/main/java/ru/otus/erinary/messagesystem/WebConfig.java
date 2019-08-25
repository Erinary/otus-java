package ru.otus.erinary.messagesystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.otus.erinary.h2.H2DataBase;
import ru.otus.erinary.messagesystem.controller.SocketHandler;
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
@ComponentScan
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer, WebSocketConfigurer {

    @Value("${datasource.url}")
    private String databaseUrl;

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
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true)
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(socketHandler(), "/ws");
    }

    @Bean
    public WebSocketHandler socketHandler() {
        return new SocketHandler();
    }
}
