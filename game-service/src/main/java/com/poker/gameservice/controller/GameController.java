package com.poker.gameservice.controller;

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
        // TODO: Complete createGame controller once game entity is defined
        return new ResponseEntity<>(
                new CreateGameResponse("gameId"),
                HttpStatus.OK);
    }
}
