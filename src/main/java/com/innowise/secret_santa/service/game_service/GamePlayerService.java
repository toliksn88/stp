package com.innowise.secret_santa.service.game_service;

import com.innowise.secret_santa.model.postgres.Game;

public interface GamePlayerService {

    Game getGameByName(String nameGame);
}