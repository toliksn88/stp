package com.innowise.secret_santa.repository;

import com.innowise.secret_santa.model.postgres.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
