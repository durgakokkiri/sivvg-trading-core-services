package com.sivvg.tradingservices.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sivvg.tradingservices.playload.ErrorDetails;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// 1️⃣ VALIDATION ERRORS (@Valid) → 400
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(field, message);
		});

		log.warn("Validation failed | path={} | errors={}", request.getDescription(false), errors);

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// 2️⃣ DB CONSTRAINT / DUPLICATE ERRORS → 400
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorDetails> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {

		String message = "Duplicate value already exists";
		Throwable root = ex.getRootCause();

		if (root != null && root.getMessage() != null) {
			String rootMsg = root.getMessage().toLowerCase();
			if (rootMsg.contains("phone")) {
				message = "Phone number already exists";
			} else if (rootMsg.contains("email")) {
				message = "Email already exists";
			} else if (rootMsg.contains("user_id") || rootMsg.contains("userid")) {
				message = "User ID already exists";
			}
		}

		log.warn("Database constraint violation | path={} | message={}", request.getDescription(false), message);

		ErrorDetails error = new ErrorDetails(new Date(), message, request.getDescription(false));

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 3️⃣ RESOURCE NOT FOUND → 404
	@ExceptionHandler({ ResourceNotFoundException.class, HomeResourceNotFoundException.class })
	public ResponseEntity<ErrorDetails> handleNotFound(RuntimeException ex, WebRequest request) {

		log.warn("Resource not found | path={} | message={}", request.getDescription(false), ex.getMessage());

		ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	// 4️⃣ BAD REQUEST / BUSINESS ERRORS → 400
	@ExceptionHandler({ BadRequestException.class, PortfolioFileValidationException.class,
			PortfolioSheetValidationException.class, IllegalArgumentException.class })
	public ResponseEntity<ErrorDetails> handleBadRequest(RuntimeException ex, WebRequest request) {

		log.warn("Bad request | path={} | message={}", request.getDescription(false), ex.getMessage());

		ErrorDetails error = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 5️⃣ ACCESS DENIED (ROLE ISSUE) → 403
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException ex, WebRequest request) {

		log.error("Access denied | path={}", request.getDescription(false));

		ErrorDetails error = new ErrorDetails(new Date(), "Access is denied", request.getDescription(false));

		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}

	// 6️⃣ FALLBACK → 500 INTERNAL SERVER ERROR
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {

		log.error("Unhandled exception | path={}", request.getDescription(false), ex);

		ErrorDetails error = new ErrorDetails(new Date(), "Internal Server Error", request.getDescription(false));

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	

	
}
