package com.sivvg.tradingservices.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.ContactUSRequest;
import com.sivvg.tradingservices.model.User;
@Repository
public interface ContactUSRequestRepository extends JpaRepository<ContactUSRequest, Long> {

	// All requests of a user
	List<ContactUSRequest> findByUser(User user);
}
