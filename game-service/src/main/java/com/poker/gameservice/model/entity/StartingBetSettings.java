package com.poker.gameservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity(name = "starting_bet_settings")
@Data
@AllArgsConstructor
public class StartingBetSettings {
    @Id
    private String id;

    @Column(nullable = false)
    private Integer growthRate;

    @Column(nullable = false)
    private Integer perRoundCount;
}
