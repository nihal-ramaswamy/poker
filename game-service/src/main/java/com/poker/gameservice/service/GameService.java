package com.poker.gameservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.GameSettings;
import com.poker.gameservice.model.Pot;
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
                new ArrayList<>(),
                null,
                null,
                gameSettings,
                null));

        return gameID;
    }

    public Game getGame(String gameID) throws GameDoesNotExistException {
        Optional<Game> gameOptional = gameRepository.findById(gameID);
        if (gameOptional.isEmpty()) {
            throw new GameDoesNotExistException();
        }
        return gameOptional.get();
    }
}
