package com.poker.gameservice.controller;

import com.poker.gameservice.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.dto.CreateGameResponse;

@RestController
@RequestMapping("/game")
public class GameController {
    private GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(name = "/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
        String gameID = gameService.createGame(request.getAdminUsername(), request.getSettings());
        return new ResponseEntity<>(
                new CreateGameResponse(gameID),
                HttpStatus.OK);
    }
}
