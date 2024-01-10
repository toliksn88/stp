package com.innowise.secret_santa.model.dto.request_dto;

import com.innowise.secret_santa.constants_message.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameRegistration {

    @NotBlank(message = Constants.NOT_NULL_FIELD)
    private String nameGame;

    private String password;
}
