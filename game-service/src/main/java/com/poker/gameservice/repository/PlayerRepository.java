package com.poker.gameservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poker.gameservice.model.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
    public Optional<Player> findByUsernameAndCurrentGameID(String username, String gameID);
}
