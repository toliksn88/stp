package com.innowise.secret_santa.service.player_services;

import com.innowise.secret_santa.model.dto.request_dto.GameRegistration;
import com.innowise.secret_santa.model.dto.request_dto.PagesDto;
import com.innowise.secret_santa.model.dto.request_dto.PlayerRequestDto;
import com.innowise.secret_santa.model.dto.response_dto.PagesDtoResponse;
import com.innowise.secret_santa.model.dto.response_dto.PlayerResponseDto;

import java.util.List;

public interface PlayerService {

    void savePlayer(GameRegistration gameRegistration, PlayerRequestDto playerRequestDto, Long idAccount);

    PlayerResponseDto getPlayerById(Long id);

    void deletePlayerByNameGame(String nameGame, Long idAccount);

    PagesDtoResponse<Object> getAllPlayers(PagesDto pages);

    PlayerResponseDto changePlayer(PlayerRequestDto playerRequestDto, Long idAccount, String nameGame);

    List<PlayerResponseDto> getAllPlayersFromGame(String nameGame, Long idAuthenticationAccount);

    List<PlayerResponseDto> getCurrentPlayers(Long idAccount);
}
