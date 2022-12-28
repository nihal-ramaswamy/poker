package com.poker.gameservice.service;

import com.poker.gameservice.model.dto.PlayerJoinAdminMessage;
import com.poker.gameservice.model.dto.StartPlayerGameState;
import com.poker.gameservice.model.entity.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MessagingService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessagingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
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
}
