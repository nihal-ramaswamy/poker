package com.poker.gameservice.model.dto;

import com.poker.gameservice.model.entity.GameSettings;

import lombok.Data;

@Data
public class CreateGameRequest {
    private String adminUsername;
    private GameSettings settings;
}
