package com.sivvg.tradingservices.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sivvg.tradingservices.model.ContactUSRequest;
import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.model.MarketHolidayEntity;
import com.sivvg.tradingservices.playload.AdminReplyDTO;
import com.sivvg.tradingservices.playload.ContactResponseDTO;
import com.sivvg.tradingservices.playload.DailyTipRequest;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.repository.MarketHolidayRepository;
import com.sivvg.tradingservices.service.AdminService;
import com.sivvg.tradingservices.service.ContactUSService;
import com.sivvg.tradingservices.service.DailyTipService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
public class AdminUserController {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private DailyTipService service;

	@Autowired
	private ContactUSService contactService;

	@Autowired
	private MarketHolidayRepository repo;

	// 🔹 ADD TIP (EQUITY / FUTURES / OPTIONS)
	@PostMapping("/add")
	public ResponseEntity<DailyTipEntity> addTip(@RequestBody DailyTipRequest request) {

		logger.info("Add Tip API called");
		logger.debug("Request payload: {}", request);

		DailyTipEntity saved = service.saveTip(request);

		logger.info("Tip saved successfully with ID: {}", saved.getId());
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	
	
	// 🔹 UPLOAD MARKET HOLIDAYS (CSV)
	@PostMapping("/upload")
	public ResponseEntity<String> uploadHolidays(@RequestParam("file") MultipartFile file) {

		logger.info("Upload Market Holidays API called");
		logger.info("File received: {}", file.getOriginalFilename());

		int count = 0;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

			String line;
			boolean firstLine = true;

			while ((line = br.readLine()) != null) {

				if (firstLine) {
					firstLine = false;
					continue;
				}

				String[] data = line.split(",");

				LocalDate date = LocalDate.parse(data[0]);
				String desc = data[1];
				Integer year = Integer.parseInt(data[2]);

				if (!repo.existsByHolidayDate(date)) {

					MarketHolidayEntity h = new MarketHolidayEntity();
					h.setHolidayDate(date);
					h.setDescription(desc);
					h.setYear(year);

					repo.save(h);
					count++;
				}
			}

			logger.info("{} holidays uploaded successfully", count);

		} catch (Exception e) {
			logger.error("Error while uploading market holidays", e);
			return ResponseEntity.badRequest().body("❌ Failed to upload holidays: " + e.getMessage());
		}

		return ResponseEntity.ok("✅ " + count + " holidays uploaded successfully!");
	}

	// 🔹 UPDATE TIP
	@PutMapping("/update/{id}")
	public ResponseEntity<DailyTipEntity> updateTip(@PathVariable Long id, @RequestBody DailyTipRequest req) {

		logger.info("Update Tip API called for ID: {}", id);

		try {
			DailyTipEntity updatedTip = service.updateTip(id, req);
			logger.info("Tip updated successfully for ID: {}", id);
			return ResponseEntity.ok(updatedTip);

		} catch (RuntimeException e) {
			logger.error("Tip not found for update, ID: {}", id, e);
			return ResponseEntity.notFound().build();
		}
	}

	// 🔹 DELETE TIP
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteTip(@PathVariable Long id) {

		logger.info("Delete Tip API called for ID: {}", id);

		try {
			service.deleteTip(id);
			logger.info("Tip deleted successfully for ID: {}", id);
			return ResponseEntity.noContent().build();

		} catch (RuntimeException e) {
			logger.error("Tip not found for delete, ID: {}", id, e);
			return ResponseEntity.notFound().build();
		}
	}

	// 🔹 GET ALL USERS
	@GetMapping("/users")
	public ResponseEntity<List<RegisterRequest>> getAllUsers() {

		logger.info("Get All Users API called");

		List<RegisterRequest> list = adminService.getAllUsers();

		logger.info("Total users fetched: {}", list.size());
		return ResponseEntity.ok(list);
	}

	// 🔹 DELETE USER
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {

		logger.info("Delete User API called for ID: {}", id);

		adminService.deleteUser(id);

		logger.info("User deleted successfully for ID: {}", id);
		return ResponseEntity.ok("User deleted successfully");
	}

	// 🔹 UPDATE USER
	@PutMapping("/users/{id}")
	public ResponseEntity<RegisterRequest> updateUser(@PathVariable("id") Long id, @RequestBody RegisterRequest req) {

		logger.info("Update User API called for ID: {}", id);
		logger.debug("Update request payload: {}", req);

		RegisterRequest updated = adminService.updateUser(id, req);

		logger.info("User updated successfully for ID: {}", id);
		return ResponseEntity.ok(updated);
	}

	// 🔹 DASHBOARD COUNTS
	@GetMapping("/user-counts")
	public ResponseEntity<Map<String, Long>> getUserCounts() {

		logger.info("Dashboard user counts API called");

		return ResponseEntity.ok(adminService.getDashboardCounts());
	}

	@GetMapping("/contacts")
	public ResponseEntity<List<ContactResponseDTO>> getAllContacts() {

		logger.info("Get All Contacts API called");
		return ResponseEntity.ok(contactService.getAllContacts());
	}

	// 2️⃣ Get contact by ID
	@GetMapping("/contacts/{id}")
	public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long id) {

		logger.info("Get Contact By ID API called for ID: {}", id);
		return ResponseEntity.ok(contactService.getContactById(id));
	}

	// 3️⃣ Reply to contact
	@PutMapping("/contacts/{id}/reply")
	public ResponseEntity<String> replyToContact(@PathVariable Long id, @Valid @RequestBody AdminReplyDTO dto) {

		logger.info("Reply to Contact API called for ID: {}", id);

		contactService.replyToContact(id, dto);

		return ResponseEntity.ok("Reply updated successfully");
	}

}
