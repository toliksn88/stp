package com.innowise.secret_santa.model.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistributionResponseDto {

    private PlayerResponseDto senderPlayer;

    private PlayerResponseDto targetPlayer;

    private GameResponseDto game;

    private LocalDateTime timeCreated;
}
