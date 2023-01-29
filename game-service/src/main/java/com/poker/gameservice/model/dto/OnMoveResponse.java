package com.poker.gameservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnMoveResponse {
    private String gameId;
    private Long playerId;
    private String playerName;
    private Long potAmount;
    private Long lastRaisedAmount;
}
