/*

 */

package org.gmu.chess.configuration;


import ca.watier.echechess.engine.factories.GameConstraintFactory;
import ca.watier.echechess.engine.interfaces.GameConstraint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfiguration {
    @Bean
    public GameConstraint gameConstraint() {
        return GameConstraintFactory.getDefaultGameConstraint();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
