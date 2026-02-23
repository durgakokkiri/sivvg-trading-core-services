package com.sivvg.tradingservices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.DeviceTokenEntity;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceTokenEntity, Long> {

	Optional<DeviceTokenEntity> findByToken(String token);
}
