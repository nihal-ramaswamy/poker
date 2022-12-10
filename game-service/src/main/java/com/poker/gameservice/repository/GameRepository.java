package com.poker.gameservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poker.gameservice.model.entity.Game;

public interface GameRepository extends JpaRepository<Game, String> {
}