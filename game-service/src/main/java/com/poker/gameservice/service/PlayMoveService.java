package com.poker.gameservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poker.gameservice.model.MoveType;
import com.poker.gameservice.model.Pot;
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

        Player player = playerRepository.findById(playerID).get();
        Game game = gameRepository.findById(gameID).get();
        List<Player> players = game.getPlayers();

        if (player.getIsPlayerTurn() && player.getIsCurrentlyPlaying()) {
            updatePlayerBasedOnMove(player, game, move, betAmount);
            updateGameBasedOnMove(game, player, move, betAmount);
            updateNextPlayer(playerID, players, game);
        }

    }

    private void updateNextPlayer(Long playerID, List<Player> players, Game game) {
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
        Long splitPot = 0L;
        if (nextPlayer.getCurrentMoney() == 0 && nextPlayer.getIsCurrentlyPlaying()) {
            nextPlayer.setIsCurrentlyPlaying(false);
            for (Player p : players) {
                splitPot += Math.min(p.getCurrentMoney(), nextPlayer.getMoneyInPot());
            }

            List<Pot> pots = game.getMiniPots();
            if (pots.size() == 0) {
                pots.add(new Pot(nextPlayer.getId(), splitPot));
            } else {
                Pot pot = pots.get(pots.size() - 1);
                pots.add(new Pot(nextPlayer.getId(), splitPot - pot.getAmount()));
            }

            game.setMiniPots(pots);
        }

        nextPlayer.setIsPlayerTurn(true);
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
        if (player.getIsLastRaisedPlayer()) {
            game.setRoundNumber(game.getRoundNumber() + 1);
        }

        if (move == MoveType.CALL) {
            game.setMoneyOnTable(game.getMoneyOnTable() + betAmount);
        } else if (move == MoveType.ALL_IN || move == MoveType.RAISE) {
            game.setMoneyOnTable(game.getMoneyOnTable() + (betAmount - player.getMoneyInPot()));
        }

        gameRepository.save(game);
    }
}