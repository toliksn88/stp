package com.innowise.secret_santa.service.game_service;

import com.innowise.secret_santa.model.dto.request_dto.GameRequestDto;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.response_dto.GameResponseDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;

import java.util.List;

public interface GameService {

    GameResponseDto createGame(GameRequestDto game, Long idAccount);

    GameResponseDto getGameById(Long idGame);

    void deleteGame (Long idAccount, String nameGame);

    PagesDtoResponse<Object> getAllGames(PagesDto pages);

    GameResponseDto changeGame(GameRequestDto game, Long idAccount, String nameGame);

    List<GameResponseDto> getAllGamesAccounts(Long idAccount);
}
