package com.poker.gameservice.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card implements Serializable {
    @Column(nullable = false)
    private String suite;

    @Column(nullable = false)
    private String rank;
}
