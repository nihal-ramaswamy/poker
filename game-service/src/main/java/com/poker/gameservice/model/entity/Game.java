package com.poker.gameservice.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "game")
@Data
public class Game {
    @Id
    @SequenceGenerator(name = "games_sequence", sequenceName = "games_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_sequence")
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
