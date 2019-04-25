/*

 */

package org.gmu.chess.services;

import ca.watier.echechess.common.enums.ChessEventMessage;
import ca.watier.echechess.common.enums.Side;
import ca.watier.echechess.common.interfaces.WebSocketService;
import ca.watier.echechess.common.responses.ChessEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


/**
 * Created by yannick on 6/10/2017.
 */

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private static final String TOPIC = "/topic/";
    private final SimpMessagingTemplate template;

    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void fireSideEvent(String uuid, Side side, ChessEventMessage evtMessage, String message) {
        if (side == null || evtMessage == null || StringUtils.isBlank(uuid) || StringUtils.isBlank(message)) {
            throw new IllegalArgumentException();
        }

        template.convertAndSend(TOPIC + uuid + '/' + side, new ChessEvent(evtMessage, message));
    }

    @Override
    public void fireSideEvent(String uuid, Side side, ChessEventMessage evtMessage, String message, Object obj) {
        if (side == null || evtMessage == null || StringUtils.isBlank(uuid)) {
            throw new IllegalArgumentException();
        }

        ChessEvent payload = new ChessEvent(evtMessage, message);
        payload.setObj(obj);
        template.convertAndSend(TOPIC + uuid + '/' + side, payload);
    }

    public void fireUiEvent(String uiUuid, ChessEventMessage evtMessage, String message) {
        template.convertAndSend(TOPIC + uiUuid, new ChessEvent(evtMessage, message));
    }


    public void fireGameEvent(String uuid, ChessEventMessage evtMessage, Object message) {
        template.convertAndSend(TOPIC + uuid, new ChessEvent(evtMessage, message));
    }

    @Override
    public void fireGameEvent(String uuid, ChessEventMessage refreshBoard) {
        template.convertAndSend(TOPIC + uuid, new ChessEvent(refreshBoard));
    }
}
