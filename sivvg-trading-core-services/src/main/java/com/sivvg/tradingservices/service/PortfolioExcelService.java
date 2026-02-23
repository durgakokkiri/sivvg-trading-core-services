package com.sivvg.tradingservices.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sivvg.tradingservices.exceptions.PortfolioFileValidationException;
import com.sivvg.tradingservices.exceptions.PortfolioSheetValidationException;
import com.sivvg.tradingservices.model.PortfolioDailyRecord;
import com.sivvg.tradingservices.model.PortfolioMonthlyUserSummary;
import com.sivvg.tradingservices.playload.PortfolioMonthlyUserDto;
import com.sivvg.tradingservices.playload.PortfolioUploadResponseDTO;
import com.sivvg.tradingservices.repository.PortfolioDailyRecordRepository;
import com.sivvg.tradingservices.repository.PortfolioUserMonthlySummaryRepository;

@Service
public class PortfolioExcelService {

	private final PortfolioDailyRecordRepository dailyRepo;
	private final PortfolioUserMonthlySummaryRepository monthlyRepo;

	public PortfolioExcelService(PortfolioDailyRecordRepository dailyRepo,
			PortfolioUserMonthlySummaryRepository monthlyRepo) {
		this.dailyRepo = dailyRepo;
		this.monthlyRepo = monthlyRepo;
	}

	private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	/* ================= MONTH AGG ================= */
	private static class MonthlyAgg {
		String username;
		String month;
		int totalPnl = 0;
		double totalPnlPercentage = 0.0;
		int totalDays = 0;
		Set<String> sectors = new HashSet<>();

		MonthlyAgg(String username, String month) {
			this.username = username;
			this.month = month;
		}
	}

	/* ================= MAIN METHOD ================= */
	@Transactional
	public PortfolioUploadResponseDTO uploadExcel(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new PortfolioFileValidationException("Excel file must not be empty");
		}

		if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
			throw new PortfolioFileValidationException("Only .xlsx Excel files are allowed");
		}

		Map<String, MonthlyAgg> monthlyMap = new LinkedHashMap<>();
		Set<String> allSectors = new HashSet<>();

		int totalDays = 0;
		int winningDays = 0;

		try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {

			if (workbook.getNumberOfSheets() == 0) {
				throw new PortfolioSheetValidationException("No sheets found in Excel");
			}

			DataFormatter formatter = new DataFormatter();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			for (Sheet sheet : workbook) {

				String sheetUsername = sheet.getSheetName().trim();

				for (Row row : sheet) {

					// Skip title + header
					if (row == null || row.getRowNum() < 2)
						continue;

					/* ---------- DAY (A) ---------- */
					String dayStr = formatter.formatCellValue(row.getCell(0)).trim();
					if (!dayStr.matches("\\d+"))
						continue;
					int day = Integer.parseInt(dayStr);

					/* ---------- DATE (C) ---------- */
					Cell dateCell = row.getCell(2);
					if (dateCell == null)
						continue;

					LocalDate tradeDate;
					try {
						if (dateCell.getCellType() == CellType.NUMERIC) {
							tradeDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
						} else {
							tradeDate = LocalDate.parse(formatter.formatCellValue(dateCell),
									DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						}
					} catch (Exception e) {
						continue;
					}

					String month = tradeDate.getMonth().name().substring(0, 1)
							+ tradeDate.getMonth().name().substring(1).toLowerCase();

					/* ---------- USERNAME (D) ---------- */
					String tempUsername = sheetUsername;

					Cell userCell = row.getCell(3);
					if (userCell != null) {
						String val = formatter.formatCellValue(userCell).trim();
						if (!val.isEmpty())
							tempUsername = val;
					}

					final String finalUsername = tempUsername;
					final String finalMonth = month;

					/* ---------- PNL (E) ---------- */
					int pnl;
					Cell pnlCell = row.getCell(4);
					try {
						if (pnlCell.getCellType() == CellType.NUMERIC) {
							pnl = (int) Math.round(pnlCell.getNumericCellValue());
						} else if (pnlCell.getCellType() == CellType.FORMULA) {
							pnl = (int) Math.round(evaluator.evaluate(pnlCell).getNumberValue());
						} else {
							pnl = (int) Math.round(Double.parseDouble(formatter.formatCellValue(pnlCell)));
						}
					} catch (Exception e) {
						continue;
					}

					/* ---------- PNL % (F) ---------- */
					double pnlPct = 0.0;
					Cell pctCell = row.getCell(5);

					try {
						if (pctCell != null) {
							if (pctCell.getCellType() == CellType.NUMERIC) {
								pnlPct = pctCell.getNumericCellValue();
							} else if (pctCell.getCellType() == CellType.FORMULA) {
								pnlPct = evaluator.evaluate(pctCell).getNumberValue();
							} else {
								String v = formatter.formatCellValue(pctCell).trim();
								if (!v.isEmpty())
									pnlPct = Double.parseDouble(v);
							}
						}
					} catch (Exception e) {
						pnlPct = 0.0;
					}

					/* ---------- SECTOR (G) ---------- */
					String sector = formatter.formatCellValue(row.getCell(6)).trim();

					/* ---------- SAVE DAILY ---------- */
					PortfolioDailyRecord record = new PortfolioDailyRecord();
					record.setUsername(finalUsername);
					record.setTradeDate(tradeDate.format(OUTPUT_DATE_FORMAT));
					record.setMonth(finalMonth);
					record.setDayNumber(day);
					record.setPnl(pnl);
					record.setPnlPercentage(pnlPct);
					record.setSector(sector);

					dailyRepo.save(record);

					totalDays++;
					if (pnl > 0)
						winningDays++;
					if (!sector.isEmpty())
						allSectors.add(sector);

					/* ---------- MONTHLY AGG ---------- */
					String key = finalUsername.toLowerCase() + "|" + finalMonth.toLowerCase();

					MonthlyAgg agg = monthlyMap.computeIfAbsent(key, k -> new MonthlyAgg(finalUsername, finalMonth));

					agg.totalPnl += pnl;
					agg.totalPnlPercentage += pnlPct;
					agg.totalDays++;
					if (!sector.isEmpty())
						agg.sectors.add(sector);
				}
			}

			/* ---------- SAVE MONTHLY ---------- */
			List<PortfolioMonthlyUserDto> users = new ArrayList<>();

			for (MonthlyAgg agg : monthlyMap.values()) {

				PortfolioMonthlyUserSummary summary = new PortfolioMonthlyUserSummary();
				summary.setUsername(agg.username);
				summary.setMonth(agg.month);
				summary.setTotalPnl(agg.totalPnl);
				summary.setUniqueSectorCount(agg.sectors.size());

				double avgPct = agg.totalDays == 0 ? 0 : round(agg.totalPnlPercentage / agg.totalDays);

				summary.setAveragePnlPercentage(avgPct);
				monthlyRepo.save(summary);

				PortfolioMonthlyUserDto dto = new PortfolioMonthlyUserDto();
				dto.setUsername(agg.username);
				dto.setMonth(agg.month);
				dto.setTotalPnl(agg.totalPnl);
				dto.setUniqueSectorCount(agg.sectors.size());
				dto.setPnlPercentage(avgPct);

				users.add(dto);
			}

			/* ---------- RESPONSE ---------- */
			PortfolioUploadResponseDTO response = new PortfolioUploadResponseDTO();
			response.setReturnRateOutOf20(totalDays == 0 ? 0 : round(((double) winningDays / totalDays) * 20));
			response.setDiversificationScoreOutOf10(round(((double) Math.min(allSectors.size(), 25) / 25) * 10));
			response.setUsers(users);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Excel processing failed: " + e.getMessage());
		}
	}

	private double round(double v) {
		return Math.round(v * 100.0) / 100.0;
	}
}
