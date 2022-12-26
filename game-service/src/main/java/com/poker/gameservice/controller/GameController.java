package com.poker.gameservice.controller;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.dto.*;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.service.GameService;
import com.poker.gameservice.service.MessagingService;
import com.poker.gameservice.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final PlayerService playerService;
    private final MessagingService messagingService;

    @Autowired
    public GameController(GameService gameService, PlayerService playerService, MessagingService messagingService) {
        this.gameService = gameService;
        this.playerService = playerService;
        this.messagingService = messagingService;
    }

    @GetMapping("/{gameID}")
    public ResponseEntity<GetGameStateResponse> getGameState(@PathVariable String gameID) {
        try {
            Game game = gameService.getGame(gameID);
            GetGameStateResponse response = new GetGameStateResponse(game);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (GameDoesNotExistException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CreateGameResponse> createGame(@RequestBody CreateGameRequest request) {
        log.info(request.getAdminUsername() + " requested to create a new game.");
        String gameID = gameService.createGame(request.getAdminUsername(), request.getSettings());
        log.info("Created a game with id: " + gameID);
        return new ResponseEntity<>(new CreateGameResponse(gameID), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<PlayerJoinResponse> joinPlayerToGame(@RequestBody PlayerJoinRequest request) {
        String gameID = request.getGameID();
        String playerUsername = request.getPlayerUsername();

        log.info("Game ID: " + request.getGameID());
        log.info("Player Username: " + request.getPlayerUsername());

        Player player = playerService.getPlayerIfExistsElseCreate(playerUsername, gameID);
        Long playerID = player.getId();
        messagingService.informPlayerJoinToAdmin(gameID, player);
        return new ResponseEntity<>(new PlayerJoinResponse(playerID), HttpStatus.OK);
    }

    @GetMapping("/{gameID}/start")
    public void startGame(@PathVariable String gameID) {
        log.info("Game ID: " + gameID + " requested to start");
        List<StartPlayerGameState> startPlayerGameStateList = gameService.startGame(gameID);
        messagingService.informAllPlayersStartGameState(startPlayerGameStateList);
    }
}
