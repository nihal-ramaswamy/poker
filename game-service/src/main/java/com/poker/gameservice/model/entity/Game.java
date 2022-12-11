package com.poker.gameservice.model.entity;

import java.util.List;

import com.poker.gameservice.model.Card;
import com.poker.gameservice.model.GameSettings;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "game")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private Long lastCalledAmount;

    @Column(nullable = false)
    private Long moneyOnTable;

    @ElementCollection
    private List<Card> cardsOnTable;

    @ElementCollection
    private List<Card> availableCardsInDeck;

    @Column(nullable = false)
    private GameSettings gameSettings;
}
