/*

 */

package org.gmu.chess.dependent;

import ca.watier.echechess.communication.redis.pojos.ServerInfoPojo;

import org.gmu.chess.repositories.UserRepository;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
@Profile("dependent-mode")
public class DependentModeConfiguration {

    private final Environment environment;
    private String rabbitIp;
    private Short rabbitPort;

    @Autowired
    public DependentModeConfiguration(Environment environment) {
        this.environment = environment;
        rabbitIp = environment.getProperty("node.rabbit.ip");
        rabbitPort = environment.getProperty("node.rabbit.port", Short.class);
    }

    @Bean
    public UserRepository userRepository() {
        throw new UnsupportedOperationException();
    }

    @Bean
    public ServerInfoPojo redisServerPojo() {
        String ip = environment.getProperty("node.redis.ip");
        Short port = environment.getProperty("node.redis.port", Short.class);

        return new ServerInfoPojo(ip, port);
    }

    @Bean
    public ServerInfoPojo rabbitMqServerPojo() {
        return new ServerInfoPojo(rabbitIp, rabbitPort);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitIp);
        factory.setPort(rabbitPort);
        factory.setUsername(environment.getProperty("node.rabbit.user"));
        factory.setPassword(environment.getProperty("node.rabbit.password"));
        return factory;
    }
}
