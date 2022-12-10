package com.poker.gameservice.repository;

import java.util.Optional;
import com.poker.gameservice.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {
    List<Player> findPlayersByCurrentGameID(String gameId);
    Optional<Player> findByUsernameAndCurrentGameID(String username, String gameID);
}
