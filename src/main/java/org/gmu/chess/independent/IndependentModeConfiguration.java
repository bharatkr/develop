/*

 */

package org.gmu.chess.independent;

import ca.watier.echechess.common.interfaces.WebSocketService;
import ca.watier.echechess.communication.redis.interfaces.GameRepository;
import ca.watier.echechess.engine.engines.GenericGameHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.gmu.chess.clients.MessageClient;
import org.gmu.chess.components.MessageActionExecutor;
import org.gmu.chess.components.StandaloneMessageHandler;
import org.gmu.chess.exceptions.UserException;
import org.gmu.chess.models.Roles;
import org.gmu.chess.models.User;
import org.gmu.chess.repositories.IndependentGameRepositoryImpl;
import org.gmu.chess.repositories.IndependentUserRepositoryImpl;
import org.gmu.chess.repositories.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("independent-mode")
public class IndependentModeConfiguration {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IndependentModeConfiguration.class);

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public IndependentModeConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public GameRepository<GenericGameHandler> gameRepository() {
        return new IndependentGameRepositoryImpl();
    }

    @Bean
    public MessageClient rabbitStandaloneClient(MessageActionExecutor actionExecutor, GameRepository<GenericGameHandler> gameRepository, ObjectMapper objectMapper) {
        return new MessageClient(new StandaloneMessageHandler(actionExecutor, gameRepository, objectMapper));
    }

    @Bean
    public MessageActionExecutor messageActionExecutor(GameRepository<GenericGameHandler> gameRepository,
                                                       WebSocketService webSocketService,
                                                       ObjectMapper objectMapper) {
        return new MessageActionExecutor(gameRepository, webSocketService, objectMapper);
    }

    @Bean
    public UserRepository userRepository() {
        IndependentUserRepositoryImpl independentUserRepositoryImpl = new IndependentUserRepositoryImpl(passwordEncoder);

        try {
            User user = new User("admin", "admin", "adminEmail");

            independentUserRepositoryImpl.addNewUserWithRole(user, Roles.ADMIN);
        } catch (UserException e) {
            LOGGER.error("Unable to create the default admin user!", e);
        }

        return independentUserRepositoryImpl;
    }
}
