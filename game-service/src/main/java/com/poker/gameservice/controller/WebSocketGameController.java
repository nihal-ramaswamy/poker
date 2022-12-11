package com.poker.gameservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.poker.gameservice.model.dto.OnMoveRequest;
import com.poker.gameservice.model.dto.OnMoveResponse;
import com.poker.gameservice.service.PlayMoveService;


@Controller
public class WebSocketGameController {

    private PlayMoveService playMoveService;

    @Autowired 
    public void setPlayMoveService(PlayMoveService playMoveService) {
        this.playMoveService = playMoveService;
    }
    
    @MessageMapping("/game/play-move")
    public void playMove(@Payload OnMoveRequest request) {
        System.out.println("Received request: " + request);
        playMoveService.playMove(request.getGameId(), request.getPlayerId(), request.getMoveType(), request.getBetAmount());
    }
    
}
