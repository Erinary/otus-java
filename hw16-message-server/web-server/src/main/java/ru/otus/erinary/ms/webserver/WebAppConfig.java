package ru.otus.erinary.ms.webserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.otus.erinary.ms.messageserver.service.SocketClient;
import ru.otus.erinary.ms.webserver.service.SpringSocketClient;
import ru.otus.erinary.ms.webserver.service.WebService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "ms")
@EnableWebSocket
public class WebAppConfig implements WebSocketConfigurer {

    private final Environment env;

    public WebAppConfig(Environment env) {
        this.env = env;
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
    public WebService webService() {
        SocketClient socketClient = clientSocket();
        WebService webService = new WebService(
                env.getRequiredProperty("ms.listened.queue"),
                env.getRequiredProperty("ms.target.queue"),
                socketClient,
                objectMapper()
        );
        socketClient.registerListener(webService);
        return webService;
    }

    @Bean
    public SocketClient clientSocket() {
        return new SpringSocketClient(
                env.getRequiredProperty("ms.listened.queue"),
                Integer.parseInt(env.getRequiredProperty("ms.server.port")),
                env.getRequiredProperty("ms.server.host")
        );
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(webService(), "/webService");
    }
}
