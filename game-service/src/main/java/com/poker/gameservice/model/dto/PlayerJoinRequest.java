package com.poker.gameservice.model.dto;

import lombok.Data;

@Data
public class PlayerJoinRequest {
    String gameID;
    String playerUsername;
}
