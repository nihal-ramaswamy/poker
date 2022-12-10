package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "card")
public class Card {
    @Id
    @SequenceGenerator(name = "cards_sequence", sequenceName = "cards_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cards_sequence")
    private Long id;

    @Column(nullable = false)
    private String suite;

    @Column(nullable = false)
    private String rank;
}
