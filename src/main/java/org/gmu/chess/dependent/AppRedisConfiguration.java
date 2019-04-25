/*

 */

package org.gmu.chess.dependent;

import ca.watier.echechess.communication.redis.configuration.RedisConfiguration;
import ca.watier.echechess.communication.redis.pojos.ServerInfoPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dependent-mode")
public class AppRedisConfiguration extends RedisConfiguration {
    @Autowired
    public AppRedisConfiguration(@Qualifier("redisServerPojo") ServerInfoPojo redisServerPojo) {
        super(redisServerPojo);
    }
}
