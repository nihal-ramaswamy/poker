package com.poker.gameservice.service;

import java.util.List;

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

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    private PlayerRepository playerRepository;

    @Autowired 
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void playMove(String gameID, Long playerID, MoveType move, Long betAmount) {
        
        // List<Player> allPlayers = playerRepository.findByCurrentGameIdOrderByIdAsc(gameID).get(); 
        //https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.details 
        Player player = playerRepository.findById(playerID).get();
        Game game = gameRepository.findById(gameID).get();


        if(player.getIsPlayerTurn() && player.getIsCurrentlyPlaying()){
            Player updatedPlayer = getPlayerBasedOnMove(move, betAmount, player, game);
            playerRepository.save( updatedPlayer );
            
            Game updatedGame = getGameBasedOnMove(move, betAmount, game, player);
            gameRepository.save(updatedGame);
        }

    }



    private Player getPlayerBasedOnMove(MoveType move, Long betAmount, Player player, Game game) {
        betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);

        switch (move) {
            case FOLD:
                return new Player(
                    player.getId(),
                    player.getCurrentGameID(),
                    player.getCurrentMoney() - betAmount,
                    player.getMoneyInPot() + betAmount,
                    false,
                    player.getIsCurrentSmallBetPlayer(),
                    player.getIsCurrentBigBetPlayer(),
                    false,
                    false,
                    player.getDeck()
                );
            case CHECK:
                return new Player(
                    player.getId(),
                    player.getCurrentGameID(),
                    player.getCurrentMoney() - betAmount,
                    player.getMoneyInPot() + betAmount,
                    player.getIsCurrentlyPlaying(),
                    player.getIsCurrentSmallBetPlayer(),
                    player.getIsCurrentBigBetPlayer(),
                    player.getIsLastRaisedPlayer(),
                    false,
                    player.getDeck()
                );
            case CALL:
                return new Player(
                    player.getId(),
                    player.getCurrentGameID(),
                    player.getCurrentMoney() - betAmount,
                    player.getMoneyInPot() + betAmount,
                    player.getIsCurrentlyPlaying(),
                    player.getIsCurrentSmallBetPlayer(),
                    player.getIsCurrentBigBetPlayer(),
                    player.getIsLastRaisedPlayer(),
                    false,
                    player.getDeck()
                );
            case RAISE:
                return new Player(
                    player.getId(),
                    player.getCurrentGameID(),
                    player.getCurrentMoney() - betAmount,
                    player.getMoneyInPot() + betAmount,
                    player.getIsCurrentlyPlaying(),
                    player.getIsCurrentSmallBetPlayer(),
                    player.getIsCurrentBigBetPlayer(),
                    true,
                    false,
                    player.getDeck()
                );
            case ALL_IN:
                return new Player(
                    player.getId(),
                    player.getCurrentGameID() + betAmount,
                    player.getCurrentMoney() - betAmount,
                    player.getMoneyInPot(),
                    player.getIsCurrentlyPlaying(),
                    player.getIsCurrentSmallBetPlayer(),
                    player.getIsCurrentBigBetPlayer(),
                    true,
                    false,
                    player.getDeck()
                );

            default:
                return player;
        }
    }

    private Long getBetAmountBasedOnMove(MoveType move, Long betAmount, Player player, Game game) {
        
        if(player.getIsCurrentSmallBetPlayer() && (move == MoveType.CALL || move == MoveType.CHECK || move == MoveType.FOLD)){
            betAmount = game.getGameSettings().getSmallBet();
        }
        else if (player.getIsCurrentBigBetPlayer() && (move == MoveType.CALL || move == MoveType.CHECK || move == MoveType.FOLD)){
            betAmount = game.getGameSettings().getBigBet();
        }
        else if (move == MoveType.ALL_IN){
            betAmount = player.getCurrentMoney() - player.getMoneyInPot();
        }
        else if (move == MoveType.RAISE){
            betAmount = betAmount + game.getLastCalledAmount();
        }
        else if (move == MoveType.CALL){
            betAmount = game.getLastCalledAmount();
        }
        else if (move == MoveType.CHECK){
            betAmount = 0L;
        }
        else if (move == MoveType.FOLD){
            betAmount = 0L;
        }

        return betAmount;
        
    }

    private Game getGameBasedOnMove(MoveType move, Long betAmount, Game game, Player player) {
        if(player.getIsLastRaisedPlayer()){
            game.setRoundNumber(game.getRoundNumber() + 1);
        }

        switch(move){
            case FOLD:
                betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
                return new Game(
                    game.getId(),
                    game.getHasGameStarted(),
                    game.getRoundNumber(),
                    game.getAdminName(),
                    game.getLastCalledAmount(),
                    game.getMoneyOnTable()+ betAmount,
                    game.getCardsOnTable(),
                    game.getAvailableCardsInDeck(),
                    game.getGameSettings()
                );
            case CHECK:
                betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
                return new Game(
                    game.getId(),
                    game.getHasGameStarted(),
                    game.getRoundNumber(),
                    game.getAdminName(),
                    game.getLastCalledAmount(),
                    game.getMoneyOnTable()+ betAmount,
                    game.getCardsOnTable(),
                    game.getAvailableCardsInDeck(),
                    game.getGameSettings()
                );
            case CALL:
                betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
                return new Game(
                    game.getId(),
                    game.getHasGameStarted(),
                    game.getRoundNumber(),
                    game.getAdminName(),
                    game.getLastCalledAmount(),
                    game.getMoneyOnTable()+ betAmount,
                    game.getCardsOnTable(),
                    game.getAvailableCardsInDeck(),
                    game.getGameSettings()
                );
            case RAISE:
                game.setLastCalledAmount(game.getLastCalledAmount() + betAmount);
                betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
                return new Game(
                    game.getId(),
                    game.getHasGameStarted(),
                    game.getRoundNumber(),
                    game.getAdminName(),
                    game.getLastCalledAmount(),
                    game.getMoneyOnTable()+ betAmount,
                    game.getCardsOnTable(),
                    game.getAvailableCardsInDeck(),
                    game.getGameSettings()
                );
            case ALL_IN:
                game.setLastCalledAmount(game.getLastCalledAmount() + betAmount);
                betAmount = getBetAmountBasedOnMove(move, betAmount, player, game);
                return new Game(
                    game.getId(),
                    game.getHasGameStarted(),
                    game.getRoundNumber(),
                    game.getAdminName(),
                    game.getLastCalledAmount(),
                    game.getMoneyOnTable()+ betAmount,
                    game.getCardsOnTable(),
                    game.getAvailableCardsInDeck(),
                    game.getGameSettings()
                );
            default:
                return game;
        }

    }

}