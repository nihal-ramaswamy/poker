package com.poker.gameservice.model.dto;

import java.util.List;

import com.poker.gameservice.model.entity.Game;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GetGameStateResponse {
    private List<PlayerInfo> players;
    private Boolean hasStarted;

    @Data
    @AllArgsConstructor
    private class PlayerInfo {
        private String username;
        private Long ID;
        private Long amount;
    }

    public GetGameStateResponse(Game game) {
        this.players = game.getPlayers()
                .stream()
                .map((player) -> new PlayerInfo(player.getUsername(), player.getId(), player.getCurrentMoney()))
                .toList();
        this.hasStarted = game.getHasGameStarted();
    }
}
