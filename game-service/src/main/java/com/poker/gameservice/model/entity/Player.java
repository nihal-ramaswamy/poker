package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "player")
public class Player {
    @Id
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    private Long playerID;

    @Column(nullable = false, unique = true)
    private Long currentGameID;

    @Column(nullable = false)
    private Long currentMoney;

    @Column(nullable = false)
    private Long moneyInPot;
}
