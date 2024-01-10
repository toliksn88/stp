package com.innowise.secret_santa.repository;

import com.innowise.secret_santa.model.postgres.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByProfileAccountId(Long idAccount);
    Player findPlayerByGameNameGameAndProfileAccountId(String nameGame, Long idAccount);

}