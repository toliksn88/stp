package com.innowise.secret_santa.model.dto.response_dto;

import com.innowise.secret_santa.model.dto.ProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerResponseDto {

    private LocalDateTime timeRegistration;

    private String necessaryThings;

    private String unnecessaryThings;

    private ProfileDto profile;

    private GameResponseDto game;
}
