package com.poker.gameservice.models;

import lombok.Data;

@Data
public class StartingBetSettings {
    private Integer growthRate;
    private Integer perRoundCount;
}
