package com.sivvg.tradingservices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.Status;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
	Optional<Status> findByName(String name);

}
