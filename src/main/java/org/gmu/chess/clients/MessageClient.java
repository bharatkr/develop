/*

 */

package org.gmu.chess.clients;

import org.springframework.amqp.rabbit.core.RabbitOperations;

public class MessageClient {
    private RabbitOperations rabbitOperations;

    public MessageClient(RabbitOperations rabbitOperations) {
        this.rabbitOperations = rabbitOperations;
    }

    public void sendMessage(String queueName, String message) {
        rabbitOperations.convertAndSend(queueName, message);
    }
}
