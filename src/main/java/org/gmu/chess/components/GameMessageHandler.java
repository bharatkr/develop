/*

 */

package org.gmu.chess.components;

import org.springframework.amqp.rabbit.annotation.RabbitListener;


public class GameMessageHandler {
    private final MessageActionExecutor actionExecutor;

    public GameMessageHandler(MessageActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @RabbitListener(queues = "#{nodeToAppMoveQueue.name}")
    public void handleMoveResponseMessage(String message) {
        actionExecutor.handleMoveResponseMessage(message);
    }

    @RabbitListener(queues = "#{nodeToAppAvailMoveQueue.name}")
    public void handleAvailMoveResponseMessage(String message) {
        actionExecutor.handleAvailMoveResponseMessage(message);
    }
}
