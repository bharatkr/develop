/*

 */

package org.gmu.chess.server;

import org.gmu.chess.common.sessions.Player;
import org.gmu.chess.common.utils.Constants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.UUID;

/**
 * Created by yannick on 4/17/2017.
 */

@Component
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        HttpSession session = httpSessionEvent.getSession();
        Player player = new Player(UUID.randomUUID().toString());
        session.setAttribute(Constants.PLAYER, player);
    }


    //Methods should not be empty
    @java.lang.SuppressWarnings("squid:S1186")
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
    }
}
