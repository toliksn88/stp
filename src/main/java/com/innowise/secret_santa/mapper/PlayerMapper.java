package com.innowise.secret_santa.mapper;

import com.innowise.secret_santa.model.dto.request_dto.PlayerRequestDto;
import com.innowise.secret_santa.model.dto.response_dto.PlayerResponseDto;
import com.innowise.secret_santa.model.postgres.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeRegistration", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "game", ignore = true)
    Player toPlayer(PlayerRequestDto playerRequestDto);


    PlayerResponseDto toPlayerResponseDto(Player player);

}
