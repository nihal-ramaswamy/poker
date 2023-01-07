package com.poker.gameservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.poker.gameservice.model.dto.OnMoveAdminMessage;
import com.poker.gameservice.model.dto.OnMovePlayerMessage;
import com.poker.gameservice.model.dto.PlayerJoinAdminMessage;
import com.poker.gameservice.model.dto.StartPlayerGameState;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.PlayerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessagingService {
    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerRepository playerRepository;

    @Autowired
    public MessagingService(SimpMessagingTemplate messagingTemplate, PlayerRepository playerRepository) {
        this.messagingTemplate = messagingTemplate;
        this.playerRepository = playerRepository;
    }

    public void informPlayerJoinToAdmin(String gameID, Player player) {
        String onJoinURL = "/admin/" + gameID + "/on-join";
        log.info("Informing admin on player " + player.getId() + " join");
        messagingTemplate.convertAndSend(onJoinURL, new PlayerJoinAdminMessage(player.getUsername(), gameID));
    }

    private void informPlayerStartGameState(StartPlayerGameState startPlayerGameState) {
        log.info("Sending start game state to " + startPlayerGameState.getPlayerId());
        String onStartUrl = "/player/" + startPlayerGameState.getPlayerId() + "/start-game-state";
        messagingTemplate.convertAndSend(onStartUrl, startPlayerGameState);
    }

    public void informAllPlayersStartGameState(List<StartPlayerGameState> startPlayerGameStateList) {
        log.info("Informing " + startPlayerGameStateList.size() + "players about commence of game");
        for (StartPlayerGameState startPlayerGameState : startPlayerGameStateList) {
            informPlayerStartGameState(startPlayerGameState);
        }
    }

    public void informAdminAndPlayersOnMovePlayed(String gameID) {
        log.info("Informing admin and players on move played for game: " + gameID);
        List<Player> players = playerRepository.findPlayersByCurrentGameID(gameID);
        informAdminOnMovePlayed(gameID, players);
        informPlayersOnMovePlayed(gameID, players);
    }

    private void informAdminOnMovePlayed(String gameID, List<Player> players) {
        log.info("Informing admin on move played");
        Long currentMovePlayerID = findCurrentMovePlayerID(players);
        Long lastRaisedPlayerID = findLastRaisedPlayerID(players);
        String onMoveURL = "/admin/" + gameID + "/on-move";
        messagingTemplate.convertAndSend(onMoveURL, new OnMoveAdminMessage(currentMovePlayerID, lastRaisedPlayerID));
    }

    private void informPlayersOnMovePlayed(String gameID, List<Player> players) {
        log.info("Informing players on move played");
        Long currentMovePlayerID = findCurrentMovePlayerID(players);
        String onMoveURL = "/player/" + gameID + "/on-move";
        messagingTemplate.convertAndSend(onMoveURL, new OnMovePlayerMessage(currentMovePlayerID));
    }

    private Long findCurrentMovePlayerID(List<Player> players) {
        for (Player player : players) {
            if (player.getIsPlayerTurn()) {
                return player.getId();
            }
        }
        return null;
    }

    private Long findLastRaisedPlayerID(List<Player> players) {
        for (Player player : players) {
            if (player.getIsLastRaisedPlayer()) {
                return player.getId();
            }
        }
        return null;
    }
}
