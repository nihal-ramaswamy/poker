package com.poker.gameservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.exception.GameDoesNotExistException;
import com.poker.gameservice.exception.PlayerDoesNotExistException;
import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.MoveType;
import com.poker.gameservice.model.Pot;
import com.poker.gameservice.model.Hand;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.GameRepository;
import com.poker.gameservice.repository.PlayerRepository;
import com.poker.gameservice.util.CardUtils;
import com.poker.gameservice.util.HandAnalyser;

@Service
public class PlayMoveService {
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private MessagingService messagingService;

    @Autowired
    public PlayMoveService(GameRepository gameRepository, PlayerRepository playerRepository,
            MessagingService messagingService) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.messagingService = messagingService;
    }

    public void playMove(String gameID, Long playerID, MoveType move, Long betAmount)
            throws PlayerDoesNotExistException, GameDoesNotExistException {
        Optional<Player> optionalPlayer = playerRepository.findById(playerID);
        if (optionalPlayer.isEmpty()) {
            throw new PlayerDoesNotExistException();
        }

        Optional<Game> optionalGame = gameRepository.findById(gameID);
        if (optionalGame.isEmpty()) {
            throw new GameDoesNotExistException();
        }

        Player player = optionalPlayer.get();
        Game game = optionalGame.get();
        List<Player> players = game.getPlayers();

        if (player.getIsPlayerTurn() && player.getIsCurrentlyPlaying()) {
            updatePlayerBasedOnMove(player, game, move, betAmount);
            updateGameBasedOnMove(game, player, move, betAmount);
            updateNextPlayer(playerID, players, game);
            messagingService.informAdminAndPlayersOnMovePlayed(gameID);
        }
    }

    private void updatePlayerBasedOnMove(Player player, Game game, MoveType move, Long betAmount) {
        Player updatedPlayer = getPlayerBasedOnMove(move, betAmount, player, game);
        playerRepository.save(updatedPlayer);
    }

    private Player getPlayerBasedOnMove(MoveType move, Long betAmount, Player player, Game game) {
        Player updatedPlayer = player.toBuilder().isPlayerTurn(false).build();

        switch (move) {
            case FOLD:
                updatedPlayer.setIsLastRaisedPlayer(false);
                updatedPlayer.setIsCurrentlyPlaying(false);
                break;
            case RAISE, ALL_IN:
                updatedPlayer.setCurrentMoney(player.getCurrentMoney() - (betAmount - updatedPlayer.getMoneyInPot()));
                updatedPlayer.setMoneyInPot(betAmount);
                updatedPlayer.setIsLastRaisedPlayer(true);
                break;
            case CALL:
                updatedPlayer.setCurrentMoney(player.getCurrentMoney() - betAmount);
                updatedPlayer.setMoneyInPot(player.getMoneyInPot() + betAmount);
                break;
            default:
        }

        return updatedPlayer;
    }

    private void updateGameBasedOnMove(Game game, Player player, MoveType move, Long betAmount) {
        if (move == MoveType.CALL) {
            game.setMoneyOnTable(game.getMoneyOnTable() + betAmount);
        } else if (move == MoveType.ALL_IN || move == MoveType.RAISE) {
            game.setMoneyOnTable(game.getMoneyOnTable() + (betAmount - player.getMoneyInPot()));
        }
    }

    private void updateNextPlayer(Long playerID, List<Player> players, Game game) {
        Player nextPlayer = findNextPlayer(playerID, players);

        if (nextPlayer.getIsLastRaisedPlayer()) {
            game.setRoundNumber(game.getRoundNumber() + 1);

            boolean isLastRound = game.getCardsOnTable().size() == 5;

            if (isLastRound) {
                HandAnalyser handAnalyser = new HandAnalyser();
                List<Entry<Long, Hand>> hands = new ArrayList<>(handAnalyser.getHands(game).entrySet());
                hands.sort((firstHand, secondHand) -> {

                    int firstRank = firstHand.getValue().getRank();
                    int secondRank = secondHand.getValue().getRank();
                    int firstScore = firstHand.getValue().getScore();
                    int secondScore = secondHand.getValue().getScore();
                
                    return firstRank == secondRank ? firstScore - secondScore : firstRank - secondRank;
                });

                List<Pot> pots = game.getMiniPots();
                int potsCount = pots.size();
                int currPotIndex = 0;
                int winnerIndex = 0;
                Long winnerId = hands.get(winnerIndex).getKey();

                for (int right = 0; right < potsCount; ++right) {
                    if (pots.get(right).getPlayerID() == winnerId) {
                        if(currPotIndex!=0){
                            pots.get(right).setAmount(pots.get(right).getAmount() - pots.get(currPotIndex).getAmount());
                            }
                        }
                        currPotIndex = right;   
                        final Long finalWinnerId = winnerId;
                        Player winner = players.stream().filter(player -> player.getId().equals(finalWinnerId)).findFirst().get();
                        winner.setCurrentMoney(winner.getCurrentMoney() + pots.get(currPotIndex).getAmount());
                        game.setMoneyOnTable(game.getMoneyOnTable() - pots.get(currPotIndex).getAmount());
                        winnerId = hands.get(++winnerIndex).getKey(); 
                    }
                    
                    final Long finalWinnerId = winnerId;
                    Player winner = players.stream().filter(player -> player.getId().equals(finalWinnerId)).findFirst().get();
                    winner.setCurrentMoney(winner.getCurrentMoney() + game.getMoneyOnTable());
                } 

            } else {
                List<Card> availableCards = game.getAvailableCardsInDeck();
                boolean isFirstRound = availableCards.isEmpty();
                if (isFirstRound) {
                    Integer numberOfDecks = game.getGameSettings().getNumberOfDecks();
                    availableCards = CardUtils.getStartingDeck(numberOfDecks);
                    List<Card> flopCards = CardUtils.getFlopCards(numberOfDecks);
                    availableCards.removeAll(flopCards);

                    game.setAvailableCardsInDeck(availableCards);
                    game.setCardsOnTable(flopCards);
                } else {
                    Card randomCard = CardUtils.getRandomCard(availableCards);
                    availableCards.remove(randomCard);
                    game.setAvailableCardsInDeck(availableCards);

                    List<Card> cardsOnTable = game.getCardsOnTable();
                    cardsOnTable.add(randomCard);
                    game.setCardsOnTable(cardsOnTable);
                }
            }

        boolean isNextPlayerAllIn = nextPlayer.getCurrentMoney() == 0 && nextPlayer.getIsCurrentlyPlaying();
        if (isNextPlayerAllIn) {
            nextPlayer.setIsCurrentlyPlaying(false);

            Long splitPot = 0L;
            for (Player player : players) {
                splitPot += Math.min(player.getCurrentMoney(), nextPlayer.getMoneyInPot());
            }

            List<Pot> pots = game.getMiniPots();
            pots.add(new Pot(nextPlayer.getId(), splitPot));
         
            game.setMiniPots(pots);
            gameRepository.save(game);
            nextPlayer = findNextPlayer(nextPlayer.getId(), players);
        }

        nextPlayer.setIsPlayerTurn(true);
        playerRepository.save(nextPlayer);
    }

    private Player findNextPlayer(Long playerID, List<Player> players) {
        Player nextPlayer = new Player();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == playerID) {
                if (i == players.size() - 1) {
                    nextPlayer = players.get(0);
                } else {
                    nextPlayer = players.get(i + 1);
                }
                break;
            }
        }
        return nextPlayer;
    }
}