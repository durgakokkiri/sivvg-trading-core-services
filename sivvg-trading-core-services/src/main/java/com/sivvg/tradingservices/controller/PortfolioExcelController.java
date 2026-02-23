package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sivvg.tradingservices.exceptions.PortfolioFileValidationException;
import com.sivvg.tradingservices.playload.PortfolioUploadResponseDTO;
import com.sivvg.tradingservices.service.PortfolioExcelService;

@RestController
@RequestMapping("/api/v1/excel")
@CrossOrigin(origins = "*")
public class PortfolioExcelController {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioExcelController.class);

	private final PortfolioExcelService excelService;

	public PortfolioExcelController(PortfolioExcelService excelService) {
		this.excelService = excelService;
	}

	/**
	 * Upload Excel file and process portfolio data
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<PortfolioUploadResponseDTO> uploadExcel(@RequestParam("file") MultipartFile file) {

		logger.info("Portfolio Excel Upload API called");

		if (file == null || file.isEmpty()) {
			logger.warn("Uploaded Excel file is empty or null");
			throw new PortfolioFileValidationException("Excel file must not be empty");
		}

		String filename = file.getOriginalFilename();

		if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
			logger.warn("Invalid Excel file format: {}", filename);
			throw new PortfolioFileValidationException("Only .xlsx Excel files are allowed");
		}

		logger.info("Valid Excel file received: {}", filename);

		PortfolioUploadResponseDTO response = excelService.uploadExcel(file);

		logger.info("Portfolio Excel processed successfully");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
