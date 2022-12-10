package com.poker.gameservice.service;

import com.poker.gameservice.model.dao.GameDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.poker.gameservice.model.dto.CreateGameRequest;
import com.poker.gameservice.model.entity.GameSettings;
import com.poker.gameservice.model.entity.StartingBetSettings;
import com.poker.gameservice.util.RandomStringGenerator;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Mock
    private CreateGameRequest request;
    
    @Mock
    private GameSettings gameSettings;

    @MockBean
    private GameDao gameDao;
    @BeforeEach
    void setUp() {
        String gameID = RandomStringGenerator.generate();

        StartingBetSettings startingBetSettings = new StartingBetSettings(gameID, 100, 100);
        gameSettings = new GameSettings(gameID, 140,  120,  130, startingBetSettings, 100,100, true);
        request = new CreateGameRequest("adminUserName",gameSettings);

        Mockito.doReturn(null).when(gameDao.gameIDExists(gameID));

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