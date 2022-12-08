package com.poker.gameservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poker.gameservice.models.dto.CreateGameRequestDTO;
import com.poker.gameservice.models.dto.CreateGameResponseDTO;

@RestController
@RequestMapping("/game")
public class GameController {
    @PostMapping
    public ResponseEntity<CreateGameResponseDTO> createGame(@RequestBody CreateGameRequestDTO request) {
        // TODO: Complete createGame controller once game table is defined
        System.out.println(request);
        ResponseEntity<CreateGameResponseDTO> response = new ResponseEntity<CreateGameResponseDTO>(
                new CreateGameResponseDTO("gameId"),
                HttpStatus.OK);
        return response;
    }
}
