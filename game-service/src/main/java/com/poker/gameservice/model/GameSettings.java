package com.poker.gameservice.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSettings implements Serializable {
    @Column(nullable = false)
    private Integer startingMoney;

    @Column(nullable = false)
    private Integer smallBet;

    @Column(nullable = false)
    private Integer bigBet;

    @Column(nullable = false)
    private StartingBetSettings startingBetSettings;

    @Column(nullable = false)
    private Integer timeLimit;

    @Column(nullable = false)
    private Integer numberOfDecks;

    @Column(nullable = false)
    private Boolean exposeWinnerDetails;

    @Column(nullable = false)
    private String currentSmallBetPlayerId;

    @Column(nullable = false)
    private String currentBigBetPlayerId;
}
