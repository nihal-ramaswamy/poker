package com.poker.gameservice.service;

import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.dto.CreateStartGameRequest;
import com.poker.gameservice.model.dto.CreateStartGameResponse;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.model.GameSettings;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.repository.GameRepository;
import com.poker.gameservice.util.RandomStringGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class GameService {
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Need to get two random players. One for small bet, one for big bet
    private List<String> getTwoRandomPlayersWithoutRepetition(List<Player> playerList) {
        List<String> randomPlayerList = new ArrayList<>();
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

    private List<Card> generateDeck() {
        List<String> suite = new ArrayList<>(List.of("Diamond", "Spade", "Club", "Heart"));
        List<String> rank = new ArrayList<>(List.of("Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"));

        List<Card> deck = new ArrayList<>();

        for (String value : suite) {
            for (String s : rank) {
                deck.add(new Card(value, s));
            }
        }

        return deck;
    }

    List<Card> getStartingDeck(Integer numDecks) {
        List<Card> availableCards = new ArrayList<>();

        for (int i = 0; i < numDecks; ++i) {
            List<Card> deck = generateDeck();
            availableCards.addAll(deck);
        }

        return availableCards;
    }

    /*
    * @param numDecks Number of decks in the game
    * */
    private List<Card> getFlopCards(Integer numDecks) {
        List<Card> availableCards = getStartingDeck(numDecks);

        Random rand = new Random();
        List<Card> chosenCards = new ArrayList<>();

        for (int i = 0; i < availableCards.size(); ++i) {
            int randomIndex = rand.nextInt(availableCards.size());

            while (chosenCards.contains(availableCards.get(randomIndex))) {
                randomIndex = rand.nextInt(availableCards.size());
            }

            chosenCards.add(availableCards.get(randomIndex));
        }

        return chosenCards;
    }

    private void updateGameInDB(Game game, List<Card> flopCards, Integer numDecks, List<String> chosenPlayers) {
        List<Card> availableCards = this.getStartingDeck(numDecks);
        availableCards.removeAll(flopCards);

        game.setCardsOnTable(flopCards);
        game.setHasGameStarted(true);
        game.setAvailableCardsInDeck(availableCards);

        GameSettings gameSettings = game.getGameSettings();
        gameSettings.setCurrentSmallBetPlayerId(chosenPlayers.get(0));
        gameSettings.setCurrentBigBetPlayerId(chosenPlayers.get(1));

        game.setGameSettings(gameSettings);

        gameRepository.save(game);
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

    public Game getGame(String gameID) throws GameDoesNotExistException {
        Optional<Game> gameOptional = gameRepository.findById(gameID);
        if (gameOptional.isEmpty()) {
            throw new GameDoesNotExistException();
        }
        return gameOptional.get();
    }

    public CreateStartGameResponse startGame(CreateStartGameRequest request) {
        Game game = this.gameRepository.findGameById(request.getGameId());
        Integer numDecks = game.getGameSettings().getNumberOfDecks();
        List<Player> playersInGame = this.playerRepository.findPlayersByCurrentGameID(request.getGameId());

        // numPlayers = 2, 1 for smallBetPlayer, 1 for bigBetPlayer
        List<String> chosenPlayers = this.getTwoRandomPlayersWithoutRepetition(playersInGame);
        List<Card> flopCards = this.getFlopCards(numDecks);

        updateGameInDB(game, flopCards, numDecks, chosenPlayers);


        // TODO: inform corresponding players about big bet and small bet
        return new CreateStartGameResponse(chosenPlayers.get(0), chosenPlayers.get(1), flopCards);
    }
}
