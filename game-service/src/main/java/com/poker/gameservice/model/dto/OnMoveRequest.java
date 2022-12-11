package com.poker.gameservice.model.dto;

import com.poker.gameservice.model.MoveType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class OnMoveRequest {

    private Long playerId;
    private String gameId;
    private MoveType moveType;
    private Long betAmount;
    
}
