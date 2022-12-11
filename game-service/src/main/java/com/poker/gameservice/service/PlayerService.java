package com.poker.gameservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.poker.gameservice.model.dto.PlayerJoinAdminMessage;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.PlayerRepository;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, SimpMessagingTemplate messagingTemplate) {
        this.playerRepository = playerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Player getPlayerIfExistsElseCreate(String username, String gameID) {
        Optional<Player> playerOptional = playerRepository.findByUsernameAndCurrentGameID(username, gameID);
        if (playerOptional.isPresent()) {
            return playerOptional.get();
        }
        Player player = new Player(null, username, gameID, 0L, 0L, false, false, false, false, false, null);
        return playerRepository.save(player);
    }

    public void informPlayerJoinToAdmin(String gameID, Player player) {
        String onJoinURL = "/admin/" + gameID + "/on-join";
        System.out.println(onJoinURL);
        messagingTemplate.convertAndSend(onJoinURL, new PlayerJoinAdminMessage(player.getUsername(), gameID));
    }
}
