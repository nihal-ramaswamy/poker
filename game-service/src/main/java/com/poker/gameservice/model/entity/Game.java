package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "game")
public class Game {
    @Id
    private String id;

    @Column(nullable = false)
    private Boolean hasGameStarted;

    @Column(nullable = false)
    private Integer roundNumber;

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false)
    private Long lastRaisedAmount;

    @Column(nullable = false)
    private Long moneyOnTable;

    @ManyToOne
    private Card cardsOnTable;

    @ManyToOne
    private Card availableCardsInDeck;

    @OneToOne
    private GameSettings gameSettings;
}
