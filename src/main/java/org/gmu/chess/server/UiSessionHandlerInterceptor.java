/*

 */

package org.gmu.chess.server;

import ca.watier.echechess.common.enums.ChessEventMessage;
import ca.watier.echechess.common.interfaces.WebSocketService;

import org.apache.commons.lang3.ArrayUtils;
import org.gmu.chess.services.UiSessionService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static ca.watier.echechess.common.utils.Constants.THE_CLIENT_LOST_THE_CONNECTION;

/**

 */
public class UiSessionHandlerInterceptor extends HandlerInterceptorAdapter {

    private UiSessionService uiSessionService;
    private WebSocketService webSocketService;

    public UiSessionHandlerInterceptor(UiSessionService uiSessionService, WebSocketService webSocketService) {
        this.uiSessionService = uiSessionService;
        this.webSocketService = webSocketService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean isAllowed = false;

        String[] uiUuids = request.getParameterValues("uiUuid");

        if (ArrayUtils.isNotEmpty(uiUuids)) { //Authorize only those with an active ui session
            String uiUuid = uiUuids[0];
            isAllowed = uiSessionService.isUiSessionActive(UUID.fromString(uiUuid));

            if (!isAllowed) {
                webSocketService.fireUiEvent(uiUuid, ChessEventMessage.UI_SESSION_EXPIRED, THE_CLIENT_LOST_THE_CONNECTION);
            }
        }

        return isAllowed;
    }
}
