package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "player")
public class Player {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String currentGameID;

    @Column(nullable = false)
    private Long currentMoney;

    @Column(nullable = false)
    private Long moneyInPot;

    @Column(nullable = false)
    private Boolean isCurrentlyPlaying;

    @Column(nullable = false)
    private Boolean isCurrentSmallBetPlayer;

    @Column(nullable = false)
    private Boolean isCurrentBigBetPlayer;

    @Column(nullable = false)
    private Boolean isLastRaisedPlayer;

    @Column(nullable = false)
    private Boolean isPlayerTurn;

    @ManyToOne
    private Card deck;
}
