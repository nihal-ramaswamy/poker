package com.poker.gameservice.model.entity;

import java.util.List;

import com.poker.gameservice.model.Card;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "player")
public class Player {
    @Id
    @GeneratedValue(generator = "player_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "sequence_gen_player_id", sequenceName = "sequence_player_id", initialValue = 0, allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "game_id")
    private String currentGameID;

    @Column(nullable = false)
    private Long currentMoney;

    @Column(nullable = false)
    private Long betMoneyInPot;

    @Column(nullable = false)
    private Boolean isCurrentlyPlaying;

    private Boolean isCurrentSmallBetPlayer;

    private Boolean isCurrentBigBetPlayer;

    @Column(nullable = false)
    private Boolean isLastRaisedPlayer;

    @Column(nullable = false)
    private Boolean isPlayerTurn;

    @ElementCollection
    private List<Card> deck;
}
