package com.poker.gameservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poker.gameservice.model.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}