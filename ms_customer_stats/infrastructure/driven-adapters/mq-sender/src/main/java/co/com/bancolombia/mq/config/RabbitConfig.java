package co.com.bancolombia.mq.config;


import com.rabbitmq.client.ConnectionFactory;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.reactivecommons.async.rabbit.config.RabbitProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class RabbitConfig {

    private final String username;
    private final String password;
    private final String host;
    private final int port;

    public RabbitConfig(@Value("${rabbit.username}") String username, @Value("${rabbit.password}") String password,
                        @Value("${rabbit.host}") String host, @Value("${rabbit.port}") int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Bean
    @Primary
    public RabbitProperties rabbitProperties(){
        var rabbitProperties = new RabbitProperties();
        rabbitProperties.setHost(host);
        rabbitProperties.setPort(port);
        rabbitProperties.setUsername(username);
        rabbitProperties.setPassword(password);
        return rabbitProperties;
    }

    @Bean
    @Profile({"dev-local"})
    public ConnectionFactoryProvider connectionTest(RabbitProperties rabbitProperties) {
        var connectionFactory = connectionGlobal(rabbitProperties);
        return () -> connectionFactory;
    }

    public ConnectionFactory connectionGlobal(RabbitProperties rabbitProperties){
        var connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return connectionFactory;
    }

}
