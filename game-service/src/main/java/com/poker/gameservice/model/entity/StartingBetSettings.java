package com.poker.gameservice.model.entity;

import jakarta.persistence.*;

@Entity(name = "starting_bet_settings")
public class StartingBetSettings {
    @Id
    @SequenceGenerator(name = "starting_bet_setting_sequence", sequenceName = "starting_bet_setting_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "starting_bet_setting_sequence")
    private Long id;

    @Column(nullable = false)
    private Integer growthRate;

    @Column(nullable = false)
    private Integer perRoundCount;
}
