package com.poker.gameservice.model.dto;

import com.poker.gameservice.model.GameSettings;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGameRequest {
    private String adminUserId;
    private GameSettings settings;
}
