/*

 */

package org.gmu.chess.dependent;

import ca.watier.echechess.common.interfaces.WebSocketService;
import ca.watier.echechess.communication.rabbitmq.configuration.RabbitMqConfiguration;
import ca.watier.echechess.communication.redis.interfaces.GameRepository;
import ca.watier.echechess.communication.redis.pojos.ServerInfoPojo;
import ca.watier.echechess.engine.engines.GenericGameHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.gmu.chess.clients.MessageClient;
import org.gmu.chess.components.GameMessageHandler;
import org.gmu.chess.components.MessageActionExecutor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dependent-mode")
public class AppRabbitConfiguration extends RabbitMqConfiguration {

    @Autowired
    public AppRabbitConfiguration(@Qualifier("rabbitMqServerPojo") ServerInfoPojo redisServerPojo) {
        super(redisServerPojo);
    }

    @Bean
    public MessageListenerAdapter messageListener(GameMessageHandler gameMessageHandler) {
        return new MessageListenerAdapter(gameMessageHandler);
    }

    @Bean
    public GameMessageHandler gameMessageHandler(MessageActionExecutor actionExecutor) {
        return new GameMessageHandler(actionExecutor);
    }

    @Bean
    public MessageActionExecutor actionExecutor(GameRepository<GenericGameHandler> gameRepository,
                                                WebSocketService webSocketService,
                                                ObjectMapper objectMapper) {
        return new MessageActionExecutor(gameRepository, webSocketService, objectMapper);
    }

    @Bean
    public MessageClient rabbitStandaloneClient(RabbitTemplate rabbitTemplate) {
        return new MessageClient(rabbitTemplate);
    }
}
