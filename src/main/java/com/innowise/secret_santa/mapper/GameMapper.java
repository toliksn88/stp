package com.innowise.secret_santa.mapper;

import com.innowise.secret_santa.model.dto.request_dto.GameRequestDto;
import com.innowise.secret_santa.model.dto.response_dto.GameResponseDto;
import com.innowise.secret_santa.model.postgres.Game;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    GameResponseDto toGameResponseDto(Game game);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeCreated", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "statusGame", ignore = true)
    @Mapping(target = "typeGame", ignore = true)
    Game toGameFromGameRequestDto(GameRequestDto gameRequestDto);
}
