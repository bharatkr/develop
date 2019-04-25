/*

 */

package org.gmu.chess.configuration;

import ca.watier.echechess.common.sessions.Player;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static ca.watier.echechess.common.utils.CacheConstants.CACHE_UI_SESSION_EXPIRY;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;

@Configuration
public class SettingConfiguration {

    @Value("${app.security.bcrypt.work-factor:16}")
    private byte bcryptWorkFactor;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(bcryptWorkFactor);
    }

    @Bean
    public CacheConfigurationBuilder<UUID, Player> uuidPlayerCacheConfiguration() {
        return newCacheConfigurationBuilder(UUID.class, Player.class, ResourcePoolsBuilder.heap(100))
                .withExpiry(CACHE_UI_SESSION_EXPIRY);
    }
}
