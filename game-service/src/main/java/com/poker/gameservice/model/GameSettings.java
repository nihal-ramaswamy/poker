package com.poker.gameservice.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameSettings implements Serializable {
    @Id
    private String id;

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
}
