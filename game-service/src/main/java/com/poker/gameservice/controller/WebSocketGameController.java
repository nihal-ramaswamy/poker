package com.poker.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.exception.PlayerDoesNotExistException;
import com.poker.gameservice.model.dto.OnMoveRequest;
import com.poker.gameservice.service.PlayMoveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebSocketGameController {

    private PlayMoveService playMoveService;

    @Autowired
    public void setPlayMoveService(PlayMoveService playMoveService) {
        this.playMoveService = playMoveService;
    }

    @MessageMapping("/game/play-move")
    public void playMove(@Payload OnMoveRequest request) {
        log.info("Received request: " + request);
        try {
            playMoveService.playMove(request.getGameId(), request.getPlayerId(), request.getMoveType(),
                    request.getBetAmount());
        } catch (PlayerDoesNotExistException | GameDoesNotExistException e) {
            e.printStackTrace();
        }
    }
}
