package com.poker.gameservice.model.dto;

import com.poker.gameservice.model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StartPlayerGameState {
    private Long playerId;
    private Long startMoney;
    private Long bigBetPlayerId;
    private Long smallBetPlayerId;
    private List<Card> deckInHand;
}
