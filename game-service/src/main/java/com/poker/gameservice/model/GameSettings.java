package com.poker.gameservice.model;

import lombok.Data;

@Data
public class GameSettings {
    private Integer startingMoney;
    private Integer smallBet;
    private Integer bigBet;
    private StartingBetSettings startingBet;
    private Integer timeLimit;
    private Integer numberOfDecks;
    private Boolean exposeWinnerDetails;
}
