package com.poker.gameservice.model;

import lombok.Data;

@Data
public class StartingBetSettings {
    private Integer growthRate;
    private Integer perRoundCount;
}
