package com.innowise.secret_santa.model.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagesDtoResponse<T> {

    private int size;

    private int page;

    private String sort;

    private List<T> dto;
}