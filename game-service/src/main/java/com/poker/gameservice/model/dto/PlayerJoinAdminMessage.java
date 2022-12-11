package com.poker.gameservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerJoinAdminMessage {
    private String playerUsername;
    private String playerID;
}
