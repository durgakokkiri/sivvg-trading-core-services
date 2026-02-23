package com.sivvg.tradingservices.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.model.NotificationEntity;
import com.sivvg.tradingservices.repository.NotificationRepository;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin("*") // mobile/web allow
@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

	@Autowired
	private NotificationRepository notificationRepository;

	
	// 🔹 GET ALL NOTIFICATIONS
	@GetMapping
	public ResponseEntity<List<NotificationEntity>> getAllNotifications() {

		logger.info("Get All Notifications API called");

		List<NotificationEntity> list = notificationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

		logger.info("Total notifications fetched: {}", list.size());
		return ResponseEntity.ok(list);
	}

	
	// 🔹 MARK AS READ
	@PutMapping("/read/{id}")
	public ResponseEntity<NotificationEntity> markAsRead(@PathVariable Long id) {

		logger.info("Mark Notification As Read API called for ID: {}", id);

		NotificationEntity n = notificationRepository.findById(id).orElseThrow(() -> {
			logger.error("Notification not found for ID: {}", id);
			return new RuntimeException("Notification Not Found");
		});

		n.setReadStatus(true);
		notificationRepository.save(n);

		logger.info("Notification marked as read for ID: {}", id);
		return ResponseEntity.ok(n);
	}

	
	// 🔹 DELETE SINGLE NOTIFICATION
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteNotification(@PathVariable Long id) {

		logger.info("Delete Notification API called for ID: {}", id);

		notificationRepository.deleteById(id);

		logger.info("Notification deleted successfully for ID: {}", id);
		return ResponseEntity.ok("Deleted Successfully");
	}

	
	// 🔹 CLEAR ALL NOTIFICATIONS
	@DeleteMapping("/clear-all")
	public ResponseEntity<String> clearAll() {

		logger.warn("Clear ALL Notifications API called");

		notificationRepository.deleteAll();

		logger.info("All notifications cleared successfully");
		return ResponseEntity.ok("All Notifications Cleared");
	}
}
