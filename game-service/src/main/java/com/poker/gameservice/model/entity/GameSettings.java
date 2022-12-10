package com.poker.gameservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "game_settings")
@Data
@AllArgsConstructor
public class GameSettings {
    @Id
    private String id;

    @Column(nullable = false)
    private Integer startingMoney;

    @Column(nullable = false)
    private Integer smallBet;

    @Column(nullable = false)
    private Integer bigBet;

    @OneToOne
    private StartingBetSettings startingBet;

    @Column(nullable = false)
    private Integer timeLimit;

    @Column(nullable = false)
    private Integer numberOfDecks;

    @Column(nullable = false)
    private Boolean exposeWinnerDetails;
}
