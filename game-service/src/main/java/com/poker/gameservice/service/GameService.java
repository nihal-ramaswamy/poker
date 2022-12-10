package com.poker.gameservice.service;

import com.poker.gameservice.model.dao.GameDao;
import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.entity.GameSettings;
import com.poker.gameservice.util.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private GameDao gameDao;

    @Autowired
    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    private String writeToGameTable(String adminUserName, GameSettings gameSettings) {
        String gameID = RandomStringGenerator.generate();

        this.gameDao.insertGame(gameID,
                false,
                0,
                adminUserName,
                0L,
                0L,
                null,
                null,
                gameSettings);

        return gameID;
    }

    public String createGame(CreateGameRequest request) {
        return writeToGameTable(request.getAdminUsername(), request.getSettings());
    }
}
