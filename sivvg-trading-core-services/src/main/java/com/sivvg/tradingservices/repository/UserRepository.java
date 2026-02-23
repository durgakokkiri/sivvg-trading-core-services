package com.sivvg.tradingservices.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sivvg.tradingservices.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserId(String userId);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByUsername(String username);

	User findByEmailOrPhoneNumber(String emailOrphoneNumber, String emailOrphoneNumber2);

	// ✅ Total users

	@Query("SELECT COUNT(u) FROM User u")
	Long countTotalUsers();

	// ✅ Count by status name (ACTIVE / DISABLED / BLOCKED)
	@Query("SELECT COUNT(u) FROM User u JOIN u.status s WHERE s.name = :statusName")
	Long countByStatusName(@Param("statusName") String statusName);

	Optional<User> findByEmail(String string);
}
