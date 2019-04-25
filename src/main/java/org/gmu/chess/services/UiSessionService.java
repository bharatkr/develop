/*

 */

package org.gmu.chess.services;

import ca.watier.echechess.common.enums.ChessEventMessage;
import ca.watier.echechess.common.interfaces.WebSocketService;
import ca.watier.echechess.common.sessions.Player;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static ca.watier.echechess.common.utils.CacheConstants.CACHE_UI_SESSION_NAME;
import static ca.watier.echechess.common.utils.Constants.REQUESTED_SESSION_ALREADY_DEFINED;
import static ca.watier.echechess.common.utils.Constants.THE_CLIENT_LOST_THE_CONNECTION;

/**
 * Created by yannick on 6/11/2017.l
 */

@Service
public class UiSessionService {

    private final Cache<UUID, Player> cacheUi;
    private final WebSocketService webSocketService;

    @Autowired
    public UiSessionService(WebSocketService webSocketService, CacheConfigurationBuilder<UUID, Player> uuidPlayerCacheConfiguration) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache(CACHE_UI_SESSION_NAME, uuidPlayerCacheConfiguration)
                .build();

        cacheManager.init();
        cacheUi = cacheManager.getCache(CACHE_UI_SESSION_NAME, UUID.class, Player.class);
        this.webSocketService = webSocketService;
    }

    public String createNewSession(Player player) {
        String uuidAsString = null;
        UUID uuid = UUID.randomUUID();

        if (!isUiSessionActive(uuid)) {
            uuidAsString = uuid.toString();
            player.addUiSession(uuid);
            cacheUi.put(uuid, player);
        } else {
            webSocketService.fireUiEvent(uuid.toString(), ChessEventMessage.UI_SESSION_ALREADY_INITIALIZED, REQUESTED_SESSION_ALREADY_DEFINED);
        }

        return uuidAsString;
    }

    public boolean isUiSessionActive(UUID uuid) {
        return cacheUi.containsKey(uuid);
    }

    public void refresh(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Player player = cacheUi.get(UUID.fromString(uuid));

        if (player == null) {
            webSocketService.fireUiEvent(uuid, ChessEventMessage.UI_SESSION_EXPIRED, THE_CLIENT_LOST_THE_CONNECTION);
        }
    }

    public Player getItemFromCache(UUID uuid) {
        return cacheUi.get(uuid);
    }
}
