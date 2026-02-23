package com.sivvg.tradingservices.service;

import org.springframework.stereotype.Service;

/**
 * Service responsible for generating and downloading portfolio performance
 * summaries in PDF / Excel / Word formats.
 */
@Service
public interface PortfolioSummaryDownloadService {

	/**
	 * 🔹 MONTHLY VIEW
	 *
	 * Downloads portfolio summary for the last N months.
	 *
	 * Example: - User selects "Monthly View" - months = 5 - Downloads last 5 months
	 * data
	 *
	 * @param username logged-in user
	 * @param months   number of months (e.g. 5)
	 * @param format   file format (pdf / excel / word)
	 * @return byte[] generated file
	 */
	public byte[] downloadSummary(String username, int months, String format);

	/**
	 * 🔹 WEEKLY VIEW
	 *
	 * Downloads portfolio summary for a specific month only.
	 *
	 * Example: - User selects "Weekly View" - Month = February - Downloads only
	 * February data
	 *
	 * @param username logged-in user
	 * @param month    month name (January, February, etc.)
	 * @param format   file format (pdf / excel / word)
	 * @return byte[] generated file
	 */
	public byte[] downloadSummaryByMonth(String username, String month, String format);
}
