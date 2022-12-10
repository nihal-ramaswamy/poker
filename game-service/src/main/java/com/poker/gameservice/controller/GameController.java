package com.poker.gameservice.controller;

import com.poker.gameservice.service.GameControllerImpl;
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
    @PostMapping(name = "/createGame")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
        GameControllerImpl gameController = new GameControllerImpl();

        String gameID = gameController.createGame(request);

        return new ResponseEntity<>(
                new CreateGameResponse(gameID),
                HttpStatus.OK);
    }
}
