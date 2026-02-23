package com.sivvg.tradingservices.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.repository.PortfolioDailyRecordRepository;
import com.sivvg.tradingservices.service.PortfolioSummaryDownloadService;

@Service
public class PortfolioSummaryDownloadServiceImpl implements PortfolioSummaryDownloadService {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioSummaryDownloadServiceImpl.class);

	private final PortfolioDailyRecordRepository dailyRecordRepository;

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public PortfolioSummaryDownloadServiceImpl(PortfolioDailyRecordRepository dailyRecordRepository) {
		this.dailyRecordRepository = dailyRecordRepository;
	}

	/*
	 * SUMMARY =========================================================
	 */
	@Override
	public byte[] downloadSummary(String username, int months, String format) {

		logger.info("Download portfolio summary started | user={}, months={}, format={}", username, months, format);

		List<PortfolioDailyRecord> userRecords = dailyRecordRepository.findByUsername(username);

		if (userRecords.isEmpty()) {
			logger.error("No portfolio data found | user={}", username);
			throw new RuntimeException("No data found for user: " + username);
		}

		LocalDate latestTradeDate = userRecords.stream().map(r -> LocalDate.parse(r.getTradeDate(), DATE_FMT))
				.max(LocalDate::compareTo).orElseThrow();

		LocalDate cutoffDate = latestTradeDate.minusMonths(months);

		List<PortfolioDailyRecord> filteredRecords = userRecords.stream().filter(r -> {
			try {
				LocalDate d = LocalDate.parse(r.getTradeDate(), DATE_FMT);
				return !d.isBefore(cutoffDate);
			} catch (Exception e) {
				logger.warn("Invalid tradeDate skipped | recordId={}", r.getId());
				return false;
			}
		}).sorted(Comparator.comparing(r -> LocalDate.parse(r.getTradeDate(), DATE_FMT))).toList();

		if (filteredRecords.isEmpty()) {
			logger.error("No records found for last {} months | user={}", months, username);
			throw new RuntimeException("No records found for last " + months + " months");
		}

		logger.info("Filtered records count: {}", filteredRecords.size());

		return switch (format.toLowerCase()) {
		case "pdf" -> {
			logger.info("Generating PDF summary");
			yield generatePdf(username, "Last " + months + " Months Performance Summary", filteredRecords);
		}
		case "excel" -> {
			logger.info("Generating Excel summary");
			yield generateExcel(username, filteredRecords);
		}
		case "word" -> {
			logger.info("Generating Word summary");
			yield generateWord(username, filteredRecords);
		}
		default -> {
			logger.error("Invalid format requested | format={}", format);
			throw new IllegalArgumentException("Invalid format: " + format);
		}
		};
	}

	/*
	 * ======================= MONTH SUMMARY ===================
	 */
	@Override
	public byte[] downloadSummaryByMonth(String username, String month, String format) {

		logger.info("Download portfolio summary by month started | user={}, month={}, format={}", username, month,
				format);

		List<PortfolioDailyRecord> userRecords = dailyRecordRepository.findByUsername(username);

		if (userRecords.isEmpty()) {
			logger.error("No portfolio data found | user={}", username);
			throw new RuntimeException("No data found for user: " + username);
		}

		List<PortfolioDailyRecord> monthRecords = userRecords.stream()
				.filter(r -> r.getMonth() != null && r.getMonth().equalsIgnoreCase(month))
				.sorted(Comparator.comparing(r -> LocalDate.parse(r.getTradeDate(), DATE_FMT))).toList();

		if (monthRecords.isEmpty()) {
			logger.error("No records found for month={} | user={}", month, username);
			throw new RuntimeException("No records found for month: " + month);
		}

		logger.info("Monthly records count: {}", monthRecords.size());

		return switch (format.toLowerCase()) {
		case "pdf" -> {
			logger.info("Generating PDF summary for month {}", month);
			yield generatePdf(username, month + " Performance Summary", monthRecords);
		}
		case "excel" -> {
			logger.info("Generating Excel summary for month {}", month);
			yield generateExcel(username, monthRecords);
		}
		case "word" -> {
			logger.info("Generating Word summary for month {}", month);
			yield generateWord(username, monthRecords);
		}
		default -> {
			logger.error("Invalid format requested | format={}", format);
			throw new IllegalArgumentException("Invalid format: " + format);
		}
		};
	}

	/*
	 * =========================================PDF GENERATION ========================
	 */
	private byte[] generatePdf(String username, String subtitle, List<PortfolioDailyRecord> data) {

		logger.debug("PDF generation started | user={}, records={}", username, data.size());

		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 36, 36, 36, 36);

			PdfWriter.getInstance(document, out);
			document.open();

			Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
			Paragraph title = new Paragraph("Portfolio Summary Report", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			document.add(Chunk.NEWLINE);

			Font infoFont = new Font(Font.FontFamily.HELVETICA, 11);
			document.add(new Paragraph("User: " + username, infoFont));
			document.add(new Paragraph("Generated On: " + LocalDate.now(), infoFont));
			document.add(new Paragraph(subtitle, infoFont));

			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setWidths(new float[] { 2.2f, 2f, 1f, 2.5f, 2f, 1.5f });

			Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

			table.addCell(header("Date", headerFont));
			table.addCell(header("Month", headerFont));
			table.addCell(header("Day", headerFont));
			table.addCell(header("Sector", headerFont));
			table.addCell(header("P&L", headerFont));
			table.addCell(header("P&L %", headerFont));

			double totalPnl = 0;

			for (PortfolioDailyRecord r : data) {
				table.addCell(r.getTradeDate());
				table.addCell(r.getMonth());
				table.addCell(String.valueOf(r.getDayNumber()));
				table.addCell(r.getSector());
				table.addCell("₹ " + r.getPnl());
				table.addCell(r.getPnlPercentage() + " %");
				totalPnl += r.getPnl();
			}

			document.add(table);
			document.add(Chunk.NEWLINE);

			Font summaryFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
			document.add(new Paragraph("Capital Amount: ₹1,00,000", summaryFont));
			document.add(new Paragraph("Total P&L: ₹" + totalPnl, summaryFont));

			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("This report is system generated.", infoFont));

			document.close();

			logger.info("PDF summary generated successfully | user={}", username);
			return out.toByteArray();

		} catch (Exception e) {
			logger.error("Error generating PDF summary | user={}", username, e);
			throw new RuntimeException("Error generating PDF summary", e);
		}
	}

	/*
	 * =============== TABLE HEADER CELL ================
	 */
	private PdfPCell header(String text, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setBackgroundColor(new BaseColor(220, 220, 220));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPadding(6);
		return cell;
	}

	/*
	 * ===================PLACEHOLDERS ====================================
	 */
	private byte[] generateExcel(String username, List<PortfolioDailyRecord> data) {
		logger.warn("Excel generation placeholder used | user={}", username);
		return ("Excel summary for " + username).getBytes();
	}

	private byte[] generateWord(String username, List<PortfolioDailyRecord> data) {
		logger.warn("Word generation placeholder used | user={}", username);
		return ("Word summary for " + username).getBytes();
	}
}
