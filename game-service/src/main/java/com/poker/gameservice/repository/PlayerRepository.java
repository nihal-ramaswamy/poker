package com.poker.gameservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poker.gameservice.model.entity.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    public List<Player> findPlayersByCurrentGameID(String gameId);

    public Optional<Player> findByUsernameAndCurrentGameID(String username, String gameID);
}
