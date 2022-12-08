package com.poker.gameservice.models.dto;

import com.poker.gameservice.models.GameSettings;

import lombok.Data;

@Data
public class CreateGameRequestDTO {
    private String adminUsername;
    private GameSettings settings;
}
