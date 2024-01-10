package com.innowise.secret_santa.service.game_service;

import com.innowise.secret_santa.model.postgres.Game;

import java.util.List;

public interface GameDistributionService {

    List<Game> getAllGamesAfterCurrentDate();

    void changeStatusGameInFinish(Game game);
}
