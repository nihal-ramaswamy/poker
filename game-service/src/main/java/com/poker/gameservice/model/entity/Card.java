package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "card")
public class Card {
    @Id
    private String id;

    @Column(nullable = false)
    private String suite;

    @Column(nullable = false)
    private String rank;
}
