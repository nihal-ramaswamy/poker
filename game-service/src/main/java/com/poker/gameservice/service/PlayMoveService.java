package com.poker.gameservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.model.MoveType;

import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.Player;
import com.poker.gameservice.repository.GameRepository;
import com.poker.gameservice.repository.PlayerRepository;

@Service
public class PlayMoveService {
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    @Autowired
    public PlayMoveService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    public void playMove(String gameID, Long playerID, MoveType move, Long betAmount) {

        // List<Player> allPlayers =
        // playerRepository.findByCurrentGameIdOrderByIdAsc(gameID).get();
        // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details

        Player player = playerRepository.findById(playerID).get();
        Game game = gameRepository.findById(gameID).get();

        if (player.getIsPlayerTurn() && player.getIsCurrentlyPlaying()) {
            updatePlayerBasedOnMove(player, game, move, betAmount);
            updateGameBasedOnMove(game, player, move, betAmount);
            // TODO: Change currently playing player and other such properties
        }
    }

    private void updatePlayerBasedOnMove(Player player, Game game, MoveType move, Long betAmount) {
        Player updatedPlayer = getPlayerBasedOnMove(move, betAmount, player, game);
        playerRepository.save(updatedPlayer);
    }

    private Player getPlayerBasedOnMove(MoveType move, Long betAmount, Player player, Game game) {
        betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);

        Player updatedPlayer = player.toBuilder()
                .isPlayerTurn(false).currentMoney(player.getCurrentMoney() - betAmount)
                .moneyInPot(player.getMoneyInPot() + betAmount)
                .build();

        switch (move) {
            case FOLD:
                updatedPlayer.setIsLastRaisedPlayer(false);
                updatedPlayer.setIsCurrentlyPlaying(false);
                break;
            case RAISE, ALL_IN:
                updatedPlayer.setIsLastRaisedPlayer(true);
                break;
            default:
        }
        return updatedPlayer;
    }

    private Long getBetAmountBasedOnMove(MoveType move, Long betAmount, Player player, Game game) {
        Boolean moveIsCallCheckOrFold = move == MoveType.CALL || move == MoveType.CHECK || move == MoveType.FOLD;
        if (player.getIsCurrentSmallBetPlayer() && moveIsCallCheckOrFold) {
            betAmount = game.getGameSettings().getSmallBet();
        } else if (player.getIsCurrentBigBetPlayer() && moveIsCallCheckOrFold) {
            betAmount = game.getGameSettings().getBigBet();
        } else if (move == MoveType.ALL_IN) {
            betAmount = player.getCurrentMoney();
        } else if (move == MoveType.RAISE) {
            betAmount = betAmount + game.getLastCalledAmount();
        } else if (move == MoveType.CALL) {
            betAmount = game.getLastCalledAmount();
        } else if (move == MoveType.CHECK) {
            betAmount = 0L;
        } else if (move == MoveType.FOLD) {
            betAmount = 0L;
        }
        return betAmount;
    }

    private void updateGameBasedOnMove(Game game, Player player, MoveType move, Long betAmount) {
        if (player.getIsLastRaisedPlayer()) {
            game.setRoundNumber(game.getRoundNumber() + 1);
        }
        if (move == MoveType.RAISE || move == MoveType.ALL_IN) {
            game.setLastCalledAmount(game.getLastCalledAmount() + betAmount);
        }
        betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
        game.setMoneyOnTable(game.getMoneyOnTable() + betAmount);
        gameRepository.save(game);
    }
}