package com.innowise.secret_santa.model.dto.request_dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.innowise.secret_santa.constants_message.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GameRequestDto {

    @FutureOrPresent(message = Constants.NOT_PAST_DATE)
    @NotNull(message = Constants.NOT_NULL_FIELD)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(required = true, example = "2021-08-20 00:00:00")
    private LocalDateTime timeStart;

    @FutureOrPresent(message = Constants.NOT_PAST_DATE)
    @NotNull(message = Constants.NOT_NULL_FIELD)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    @ApiModelProperty(required = true, example = "2021-08-20 00:00:00")
    private LocalDateTime timeEnd;

    private String description;

    @NotBlank(message = Constants.NOT_NULL_FIELD)
    private String nameGame;

    private String password;
}
