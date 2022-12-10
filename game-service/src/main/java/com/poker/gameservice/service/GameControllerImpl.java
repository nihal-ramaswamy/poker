package com.poker.gameservice.service;

import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.entity.GameSettings;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.util.RandomStringGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class GameControllerImpl {
    private String writeToGameTable(String adminUserName, GameSettings gameSettings) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("game");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        String gameID = RandomStringGenerator.generate();


        Game game = new Game();

        game.setId(gameID);
        game.setHasGameStarted(false);
        game.setRoundNumber(0);
        game.setAdminName(adminUserName);
        game.setLastRaisedAmount(0L);
        game.setMoneyOnTable(0L);
        game.setCardsOnTable(null);
        game.setAvailableCardsInDeck(null);
        game.setGameSettings(gameSettings);
        entityManager.persist(game);

        entityManagerFactory.close();
        entityManager.close();

        return gameID;
    }

    public String createGame(CreateGameRequest request) {
        return writeToGameTable(request.getAdminUsername(), request.getSettings());
    }
}
