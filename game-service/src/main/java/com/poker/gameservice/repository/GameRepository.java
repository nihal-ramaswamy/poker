package com.poker.gameservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poker.gameservice.model.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, String> {
    Game findGameById(String gameId);
}