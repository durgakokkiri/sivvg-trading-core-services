package com.sivvg.tradingservices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	public Optional<com.sivvg.tradingservices.model.Role> findByName(String name);

	boolean existsByName(String name);
}
