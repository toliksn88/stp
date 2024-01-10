package com.innowise.secret_santa.model.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemMessageDto {

    private String textMessage;

    private String typeMessage;
}
