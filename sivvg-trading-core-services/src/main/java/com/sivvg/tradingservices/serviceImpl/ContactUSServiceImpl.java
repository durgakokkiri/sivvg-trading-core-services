package com.sivvg.tradingservices.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.model.ContactUSRequest;
import com.sivvg.tradingservices.model.User;
import com.sivvg.tradingservices.playload.AdminReplyDTO;
import com.sivvg.tradingservices.playload.ContactResponseDTO;
import com.sivvg.tradingservices.playload.ContactUSRequestDto;
import com.sivvg.tradingservices.repository.ContactUSRequestRepository;
import com.sivvg.tradingservices.repository.UserRepository;
import com.sivvg.tradingservices.service.ContactUSService;
import com.sivvg.tradingservices.service.EmailService;
import com.sivvg.tradingservices.util.ContactUSEmailTemplateUtil;

@Service
public class ContactUSServiceImpl implements ContactUSService {

	private static final Logger logger = LoggerFactory.getLogger(ContactUSServiceImpl.class);

	@Autowired
	private ContactUSRequestRepository contactRequestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public void createContactRequest(ContactUSRequestDto dto, String userId) {

		logger.info("Create Contact request service started | userId={}, type={}", userId, dto.getType());

		// 1️⃣ Fetch logged-in user
		User user = userRepository.findByUserId(userId).orElseThrow(() -> {
			logger.error("User not found while creating contact request | userId={}", userId);
			return new RuntimeException("User not found");
		});

		// 2️⃣ Create contact request entity
		ContactUSRequest contact = new ContactUSRequest();
		contact.setUser(user);
		contact.setMessage(dto.getMessage());
		contact.setType(dto.getType()); // CALLBACK / EMAIL / CHAT
		contact.setStatus("NEW");
		contact.setCreatedAt(LocalDateTime.now());
		contact.setAutoReplySent(false);

		// 3️⃣ Save contact request
		contactRequestRepository.save(contact);
		logger.info("Contact request saved successfully | contactId={}", contact.getId());

		// 4️⃣ Generate automatic email
		String subject = ContactUSEmailTemplateUtil.subject(dto.getType());
		String body = ContactUSEmailTemplateUtil.body(dto.getType(), user);

		logger.debug("Auto-reply email generated | type={}", dto.getType());

		// 5️⃣ Send email
		try {
			emailService.sendSimpleMessage(user.getEmail(), subject, body);
			logger.info("Auto-reply email sent successfully | email={}", user.getEmail());
		} catch (Exception e) {
			logger.error("Failed to send auto-reply email | email={}", user.getEmail(), e);
		}

		// 6️⃣ Mark auto-reply sent
		contact.setAutoReplySent(true);
		contactRequestRepository.save(contact);

		logger.info("Contact request completed successfully | contactId={}", contact.getId());
	}

	@Override
	public List<ContactResponseDTO> getAllContacts() {

	    logger.info("Fetching all contact requests");

	    List<ContactUSRequest> contacts = contactRequestRepository.findAll();
	    logger.debug("Total contacts fetched from DB: {}", contacts.size());

	    List<ContactResponseDTO> responseList = new ArrayList<>();

	    for (ContactUSRequest contact : contacts) {

	        ContactResponseDTO dto = new ContactResponseDTO();
	        dto.setId(contact.getId());
	        dto.setMessage(contact.getMessage());
	        dto.setStatus(contact.getStatus());
	        dto.setAdminReply(contact.getAdminReply());

	        responseList.add(dto);
	    }

	    logger.info("Successfully mapped {} contact requests to DTO", responseList.size());

	    return responseList;
	}

	@Override
	public ContactResponseDTO getContactById(Long id) {

	    logger.info("Fetching contact request by ID: {}", id);

	    ContactUSRequest contact = contactRequestRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("Contact request not found for ID: {}", id);
	                return new RuntimeException("Contact request not found");
	            });

	    logger.debug("Contact found | ID={}, Status={}", contact.getId(), contact.getStatus());

	    ContactResponseDTO dto = new ContactResponseDTO();
	    dto.setId(contact.getId());
	    dto.setMessage(contact.getMessage());
	    dto.setStatus(contact.getStatus());
	    dto.setAdminReply(contact.getAdminReply());

	    logger.info("Contact request successfully retrieved for ID: {}", id);

	    return dto;
	}


	@Override
	public void replyToContact(Long id, AdminReplyDTO dto) {

	    logger.info("Replying to contact request | ID={}, Status={}", id, dto.getStatus());

	    ContactUSRequest contact = contactRequestRepository.findById(id)
	            .orElseThrow(() -> {
	                logger.error("Cannot reply. Contact request not found for ID: {}", id);
	                return new RuntimeException("Contact request not found");
	            });

	    contact.setStatus(dto.getStatus());
	    contact.setAdminReply(dto.getReplyMessage());

	    contactRequestRepository.save(contact);

	    logger.info("Reply updated successfully | ID={}, NewStatus={}", id, dto.getStatus());
	}
	    
	    
}
