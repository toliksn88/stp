package com.innowise.secret_santa.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum StatusGame {

    START("START"),
    FINISH("FINISH");

    private String status;

    StatusGame(String status) {
        this.status = status;
    }
}
