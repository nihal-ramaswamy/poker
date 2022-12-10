package com.poker.gameservice.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class StartingBetSettings implements Serializable {
    private Integer growthAmount;
    private Integer perRoundCount;
}
