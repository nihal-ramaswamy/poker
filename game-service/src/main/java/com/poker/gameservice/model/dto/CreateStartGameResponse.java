package com.poker.gameservice.model.dto;

import com.poker.gameservice.model.Card;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateStartGameResponse {
    private String smallBetPlayerId;
    private String bigBetPlayerId;
    private List<Card> flopCards;
}
