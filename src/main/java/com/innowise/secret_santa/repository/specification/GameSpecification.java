package com.innowise.secret_santa.repository.specification;


import com.innowise.secret_santa.model.postgres.Account_;
import com.innowise.secret_santa.model.postgres.Game;
import com.innowise.secret_santa.model.postgres.Game_;
import com.innowise.secret_santa.model.postgres.Player_;
import com.innowise.secret_santa.model.postgres.Profile_;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class GameSpecification {

    protected GameSpecification() {
    }

    public static Specification<Game> likeAccountId(Long idAccount) {

        return ((root, query, criteriaBuilder) -> Optional.ofNullable(idAccount)
                .map(id -> criteriaBuilder.equal(root
                        .join(Game_.PLAYERS)
                        .join(Player_.PROFILE)
                        .get(Profile_.ACCOUNT)
                        .get(Account_.ID), id))
                .orElseGet(criteriaBuilder::conjunction));
    }
}