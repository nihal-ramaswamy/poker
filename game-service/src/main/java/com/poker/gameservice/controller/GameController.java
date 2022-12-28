package com.poker.gameservice.controller;

import java.util.List;

import com.poker.gameservice.exception.NotEnoughPlayersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.dto.CreateGameResponse;
import com.poker.gameservice.model.dto.GetGameStateResponse;
import com.poker.gameservice.model.dto.PlayerJoinRequest;
import com.poker.gameservice.model.dto.PlayerJoinResponse;
import com.poker.gameservice.model.dto.StartPlayerGameState;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.service.GameService;
import com.poker.gameservice.service.MessagingService;
import com.poker.gameservice.service.PlayerService;

import lombok.extern.slf4j.Slf4j;

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
    public ResponseEntity<String> startGame(@PathVariable String gameID) {
        try {
            log.info("Game ID: " + gameID + " requested to start");
            List<StartPlayerGameState> startPlayerGameStateList = gameService.startGame(gameID);
            messagingService.informAllPlayersStartGameState(startPlayerGameStateList);
            return ResponseEntity.ok("Informed all players about start of game.");
        } catch (NotEnoughPlayersException e) {
            return new ResponseEntity<>("Not enough players in the game to begin", HttpStatus.BAD_REQUEST);
        }
    }
}
