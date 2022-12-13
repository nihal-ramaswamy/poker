package com.poker.gameservice.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSettings implements Serializable {
    @Column(nullable = false)
    private Long startingMoney;

    @Column(nullable = false)
    private Long smallBet;

    @Column(nullable = false)
    private Long bigBet;

    @Column(nullable = false)
    private StartingBetSettings startingBetSettings;

    @Column(nullable = false)
    private Long lastRaisedAmount;

    @Column(nullable = false)
    private Integer timeLimit;

    @Column(nullable = false)
    private Integer numberOfDecks;

    @Column(nullable = false)
    private Boolean exposeWinnerDetails;
}
