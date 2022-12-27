package com.poker.gameservice.service;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.GameSettings;
import com.poker.gameservice.model.dto.StartPlayerGameState;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.GameRepository;
import com.poker.gameservice.repository.PlayerRepository;
import com.poker.gameservice.util.CardUtils;
import com.poker.gameservice.util.RandomStringGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    // Need to get two random players. One for small bet, one for big bet
    private List<Long> getTwoRandomPlayersWithoutRepetition(List<Player> playerList) {
        List<Long> randomPlayerList = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < 2; ++i) {
            int randomIndex = rand.nextInt(playerList.size());

            while (randomPlayerList.contains(playerList.get(randomIndex).getId())) {
                randomIndex = rand.nextInt(playerList.size());
            }

            randomPlayerList.add(playerList.get(randomIndex).getId());
        }

        return randomPlayerList;
    }

    private void updateGameInDB(Game game, List<Card> availableCards, List<Long> chosenPlayers) {
        game.setCardsOnTable(null);
        game.setHasGameStarted(true);
        game.setAvailableCardsInDeck(availableCards);

        GameSettings gameSettings = game.getGameSettings();
        gameSettings.setCurrentSmallBetPlayerId(chosenPlayers.get(0));
        gameSettings.setCurrentBigBetPlayerId(chosenPlayers.get(1));

        game.setGameSettings(gameSettings);

        gameRepository.save(game);
    }

    private void updatePlayerInDB(Player player, Long bigBetPlayer, Long smallBetPlayer, Long startMoney,
            List<Card> deckInHand) {
        Boolean isBigBetPlayer = Objects.equals(bigBetPlayer, player.getId());
        Boolean isSmallBetPlayer = Objects.equals(smallBetPlayer, player.getId());

        player.setCurrentMoney(startMoney);
        player.setBetMoneyInPot(0L);
        player.setIsLastRaisedPlayer(isSmallBetPlayer);
        player.setIsPlayerTurn(isSmallBetPlayer);
        player.setIsCurrentSmallBetPlayer(isSmallBetPlayer);
        player.setIsCurrentBigBetPlayer(isBigBetPlayer);
        player.setDeck(deckInHand);
        player.setIsCurrentlyPlaying(true);

        playerRepository.save(player);
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
                new ArrayList<>(),
                null,
                null,
                gameSettings));

        return gameID;
    }

    public List<StartPlayerGameState> startGame(String gameID) {
        Game game = this.gameRepository.findGameById(gameID);
        log.info(String.valueOf(game));
        Integer numDecks = game.getGameSettings().getNumberOfDecks();
        Long startMoney = game.getGameSettings().getStartingMoney();

        List<Player> playersInGame = playerRepository.findPlayersByCurrentGameID(gameID);
        log.info(String.valueOf(playersInGame));

        List<Long> chosenPlayers = getTwoRandomPlayersWithoutRepetition(playersInGame);
        Long smallBetPlayer = chosenPlayers.get(0);
        Long bigBetPlayer = chosenPlayers.get(1);

        List<Card> availableCards = CardUtils.getStartingDeck(numDecks);

        List<StartPlayerGameState> startPlayerGameStateList = new ArrayList<>();

        for (Player player : playersInGame) {
            List<Card> deckInHand = CardUtils.getHandCards(availableCards);
            availableCards.removeAll(deckInHand);

            StartPlayerGameState startPlayerGameState = new StartPlayerGameState(player.getId(), startMoney,
                    bigBetPlayer, smallBetPlayer, deckInHand);

            updatePlayerInDB(player, bigBetPlayer, smallBetPlayer, startMoney, deckInHand);

            startPlayerGameStateList.add(startPlayerGameState);
        }

        updateGameInDB(game, availableCards, chosenPlayers);

        return startPlayerGameStateList;
    }

    public Game getGame(String gameID) throws GameDoesNotExistException {
        Optional<Game> gameOptional = gameRepository.findById(gameID);
        if (gameOptional.isEmpty()) {
            throw new GameDoesNotExistException();
        }
        return gameOptional.get();
    }
}
