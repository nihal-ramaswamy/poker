package com.poker.gameservice.model.entity;

import java.util.List;

import com.poker.gameservice.model.Card;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "player")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    @Id
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
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

    @ElementCollection
    private List<Card> deck;
}
