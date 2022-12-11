package com.poker.gameservice.controller;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.dto.*;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.service.GameService;
import com.poker.gameservice.service.MessagingService;
import com.poker.gameservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.dto.CreateGameResponse;
import com.poker.gameservice.model.dto.GetGameStateResponse;
import com.poker.gameservice.model.dto.PlayerJoinRequest;
import com.poker.gameservice.model.dto.PlayerJoinResponse;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.service.GameService;
import com.poker.gameservice.service.PlayerService;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    private GameService gameService;
    private PlayerService playerService;

    private MessagingService messagingService;

    @Autowired
    public void setGameService(GameService gameService, PlayerService playerService, MessagingService messagingService) {
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
        String gameID = gameService.createGame(request.getAdminUsername(), request.getSettings());
        return new ResponseEntity<>(new CreateGameResponse(gameID), HttpStatus.OK);
    }

    @PostMapping("/join")
    public ResponseEntity<PlayerJoinResponse> joinPlayerToGame(@RequestBody PlayerJoinRequest request) {
        String gameID = request.getGameID(), playerUsername = request.getPlayerUsername();
        Player player = playerService.getPlayerIfExistsElseCreate(playerUsername, gameID);
        Long playerID = player.getId();
        playerService.informPlayerJoinToAdmin(gameID, player);
        return new ResponseEntity<>(new PlayerJoinResponse(playerID), HttpStatus.OK);
    }

    @MessageMapping("/start")
    @SendTo("/topic/startGame")
    public CreateStartGameResponse startGame(@RequestBody CreateStartGameRequest request) {
        CreateStartGameResponse startGameResponse = this.gameService.startGame(request);
        return startGameResponse;
    @MessageMapping("/start/{gameID}")
    public void startGame(@PathVariable String gameID) {
        List<StartPlayerGameState> startPlayerGameStateList = gameService.startGame(gameID);
        messagingService.informAllPlayersStartGameState(startPlayerGameStateList);
    }

    @PostMapping("/join")
    public ResponseEntity<PlayerJoinResponse> joinPlayerToGame(@RequestBody PlayerJoinRequest request) {
        String gameID = request.getGameID(), playerUsername = request.getPlayerUsername();
        Player player = playerService.getPlayerIfExistsElseCreate(playerUsername, gameID);
        Long playerID = player.getId();
        messagingService.informPlayerJoinToAdmin(gameID, player);
        return new ResponseEntity<>(new PlayerJoinResponse(playerID), HttpStatus.OK);
    }
}
