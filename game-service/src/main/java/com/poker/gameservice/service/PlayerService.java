package com.poker.gameservice.service;

import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player getPlayerIfExistsElseCreate(String username, String gameID) {
        Optional<Player> playerOptional = playerRepository.findByUsernameAndCurrentGameID(username, gameID);
        if (playerOptional.isPresent()) {
            return playerOptional.get();
        }
        Player player = new Player(null, username, gameID, 0L, 0L, false, false, false, false, false, null);
        return playerRepository.save(player);
    }
}
