package com.poker.gameservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnMoveAdminMessage {
    private Long currentMovePlayerID;
    private Long lastRaisedPlayerID;
}
