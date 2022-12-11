package com.poker.gameservice.service;

import com.poker.gameservice.model.dto.PlayerJoinAdminMessage;
import com.poker.gameservice.model.dto.StartPlayerGameState;
import com.poker.gameservice.model.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessagingService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessagingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void informPlayerJoinToAdmin(String gameID, Player player) {
        String onJoinURL = "/admin/" + gameID + "/on-join";
        messagingTemplate.convertAndSend(onJoinURL, new PlayerJoinAdminMessage(player.getUsername(), gameID));
    }

    private void informPlayerStartGameState(StartPlayerGameState startPlayerGameState) {
        String onStartUrl = "/player/" + startPlayerGameState.getPlayerId() + "/start-game-state";
        messagingTemplate.convertAndSend(onStartUrl, startPlayerGameState);

    }

    public void informAllPlayersStartGameState(List<StartPlayerGameState> startPlayerGameStateList) {
        for (StartPlayerGameState startPlayerGameState: startPlayerGameStateList) {
            informPlayerStartGameState(startPlayerGameState);
        }
    }
}
