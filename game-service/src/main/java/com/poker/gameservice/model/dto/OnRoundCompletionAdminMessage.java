package com.poker.gameservice.model.dto;

import java.util.List;

import com.poker.gameservice.model.Card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnRoundCompletionAdminMessage {
    private Long currentSmallBetPlayerID;
    private Long currentMovePlayerID;
    private List<Card> cardsOnTable;
}
