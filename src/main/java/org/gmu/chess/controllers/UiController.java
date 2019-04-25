/*

 */

package org.gmu.chess.controllers;

import ca.watier.echechess.common.pojos.Ping;
import ca.watier.echechess.common.responses.StringResponse;
import ca.watier.echechess.common.utils.SessionUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.gmu.chess.services.UiSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.gmu.chess.controllers.GameController.UI_UUID_PLAYER;

import javax.servlet.http.HttpSession;

/**
 * Created by yannick on 4/22/2017.
 */

@RestController
@RequestMapping("/api/ui")
public class UiController {
    private final UiSessionService uiSessionService;

    @Autowired
    public UiController(UiSessionService uiSessionService) {
        this.uiSessionService = uiSessionService;
    }

    @ApiOperation("Create and bind a ui session to the player")
    @GetMapping(path = "/id/1", produces = MediaType.APPLICATION_JSON_VALUE)
    public StringResponse createNewGame(HttpSession session) {
        return new StringResponse(uiSessionService.createNewSession(SessionUtils.getPlayer(session)));
    }

    @ApiOperation("Used to update the user ping timer")
    @MessageMapping("/api/ui/ping")
    @SendTo("/topic/ping")
    public void ping(@ApiParam(value = UI_UUID_PLAYER, required = true) Ping uuid) {
        uiSessionService.refresh(uuid.getUuid());
    }
}
