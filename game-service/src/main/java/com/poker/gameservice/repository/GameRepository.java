package com.poker.gameservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poker.gameservice.model.entity.Game;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    Game findGameById(String gameId);
}