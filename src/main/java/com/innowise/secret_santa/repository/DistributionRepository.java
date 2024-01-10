package com.innowise.secret_santa.repository;

import com.innowise.secret_santa.model.postgres.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution,Long> {

    List<Distribution> findAllBySenderPlayerProfileAccountId(Long accountId);

    Distribution findDistributionByGameNameGameAndSenderPlayerProfileAccountId(String nameGame, Long accountId);

    List<Distribution> findAllByGameNameGameAndGameOrganizerAccountId(String nameGame, Long accountId);
}
