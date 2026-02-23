package com.sivvg.tradingservices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivvg.tradingservices.service.PortfolioSummaryDownloadService;

@RestController
@RequestMapping("/api/v1/summary")
@CrossOrigin(origins = "*")
public class PortfolioSummaryDownloadController {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioSummaryDownloadController.class);

	private final PortfolioSummaryDownloadService summaryDownloadService;

	public PortfolioSummaryDownloadController(PortfolioSummaryDownloadService summaryDownloadService) {
		this.summaryDownloadService = summaryDownloadService;
	}

	/**
	 * DOWNLOAD PORTFOLIO SUMMARY
	 *
	 * 🔹 WEEKLY (Single Month)
	 * /api/v1/summary/download?user=Arjun&month=February&format=pdf
	 *
	 * 🔹 MONTHLY (Last N Months)
	 * /api/v1/summary/download?user=Arjun&months=5&format=excel
	 */
	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadSummary(@RequestParam(name = "user") String user,
			@RequestParam(name = "months", required = false) Integer months,
			@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "format") String format) {

		logger.info("Portfolio summary download API called | user={}, month={}, months={}, format={}", user, month,
				months, format);

		byte[] fileData;
		String fileName;
		MediaType mediaType;

		// ✅ WEEKLY → SINGLE MONTH
		if (month != null && !month.trim().isEmpty()) {

			logger.debug("Downloading summary for single month");

			fileData = summaryDownloadService.downloadSummaryByMonth(user, month, format);

			fileName = user + "_" + month + "_summary." + format;
		}
		// ✅ MONTHLY → LAST N MONTHS
		else if (months != null && months > 0) {

			logger.debug("Downloading summary for last {} months", months);

			fileData = summaryDownloadService.downloadSummary(user, months, format);

			fileName = user + "_" + months + "_months_summary." + format;
		}
		// ❌ INVALID REQUEST
		else {
			logger.error("Invalid download request: neither month nor months provided");
			throw new IllegalArgumentException("Invalid request: Provide either 'month' or 'months'");
		}

		// ✅ SET CONTENT TYPE
		if ("pdf".equalsIgnoreCase(format)) {
			mediaType = MediaType.APPLICATION_PDF;
		} else if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
			mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		} else if ("word".equalsIgnoreCase(format) || "docx".equalsIgnoreCase(format)) {
			mediaType = MediaType
					.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		} else {
			logger.warn("Unknown format received: {}", format);
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		}

		logger.info("Portfolio summary file generated successfully | fileName={}", fileName);

		return ResponseEntity.ok().contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(fileData);
	}
}
