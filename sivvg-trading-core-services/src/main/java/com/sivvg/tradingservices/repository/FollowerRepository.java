package com.sivvg.tradingservices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.Followers;
@Repository
public interface FollowerRepository extends JpaRepository<Followers, Long> {

}
