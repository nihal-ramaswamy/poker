package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "game_settings")
public class GameSettings {
    @Id
    @SequenceGenerator(name = "game_settings_sequence", sequenceName = "game_settings_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_settings_sequence")
    private Long id;

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
