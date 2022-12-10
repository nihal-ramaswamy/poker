package com.poker.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.model.GameSettings;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.repository.GameRepository;
import com.poker.gameservice.util.RandomStringGenerator;

@Service
public class GameService {
    private GameRepository gameRepository;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public String createGame(String adminUserName, GameSettings gameSettings) {
        String gameID;
        do {
            gameID = RandomStringGenerator.generate();
        } while (gameRepository.findById(gameID).isPresent());

        gameRepository.save(new Game(
                gameID,
                false,
                0,
                adminUserName,
                0L,
                0L,
                null,
                null,
                gameSettings));

        return gameID;
    }
}
