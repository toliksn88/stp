package com.innowise.secret_santa.model.dto.request_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagesDto {


    private Integer size;

    private Integer page;

    private String sort;
}
