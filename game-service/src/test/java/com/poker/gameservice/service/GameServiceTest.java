package com.poker.gameservice.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.entity.GameSettings;
import com.poker.gameservice.model.entity.StartingBetSettings;
import com.poker.gameservice.util.RandomStringGenerator;

import static org.junit.jupiter.api.Assertions.*;


class GameServiceTest {

    CreateGameRequest request;
    GameSettings gameSettings;
    @BeforeEach
    void setUp() {

        String gameID = RandomStringGenerator.generate();

        StartingBetSettings startingBetSettings = new StartingBetSettings(gameID, Mockito.mock(Integer.class), Mockito.mock(Integer.class));
        gameSettings = new GameSettings(gameID,Mockito.mock(Integer.class), Mockito.mock(Integer.class), Mockito.mock(Integer.class), startingBetSettings, Mockito.mock(Integer.class),Mockito.mock(Integer.class), true);
        request = new CreateGameRequest("adminUserName",gameSettings);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createGame() {
        GameService gameService = new GameService();
        String gameID = gameService.createGame(request);
        assertNotNull(gameID);
    }
}