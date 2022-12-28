package com.poker.gameservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class  PlayerJoinRequest {
    String gameID;
    String playerUsername;
}
