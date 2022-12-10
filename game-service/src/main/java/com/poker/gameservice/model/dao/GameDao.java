package com.poker.gameservice.model.dao;

import com.poker.gameservice.model.entity.Card;
import com.poker.gameservice.model.entity.Game;
import com.poker.gameservice.model.entity.GameSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameDao extends JpaRepository<Game, String> {
    @Modifying
    @Query(value = "INSERT INTO game(id, hasGameStarted, roundNumber, adminName, lastRaisedAmount, moneyOnTable, cardsOnTable, availableCardsInDeck, gameSettings) VALUES (:id, :hasGameStarted, :roundNumber, :adminName, :lastRaisedAmount, :moneyOnTable, :cardsOnTable, :availableCardsInDeck, :gameSettings)",
            nativeQuery = true)

    void insertGame(@Param("id") String id,
                    @Param("hasGameStarted") Boolean hasGameStarted,
                    @Param("roundNumber") Integer roundNumber,
                    @Param("adminName") String adminName,
                    @Param("lastRaisedAmount") Long lastRaisedAmount,
                    @Param("moneyOnTable") Long moneyOnTable,
                    @Param("cardsOnTable") Card cardsOnTable,
                    @Param("availableCardsInDeck") Card availableCardsInDeck,
                    @Param("gameSettings") GameSettings gameSettings);

    @Query(value = "SELECT id FROM game WHERE id = :id", nativeQuery = true)
    String gameIDExists(@Param("id") String id);

        
}