package com.poker.gameservice.model.dao;

import com.poker.gameservice.model.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameDao extends CrudRepository<Game, String> {

}
