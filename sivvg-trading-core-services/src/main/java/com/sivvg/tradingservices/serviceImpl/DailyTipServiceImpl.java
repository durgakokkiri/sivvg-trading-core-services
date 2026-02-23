package com.sivvg.tradingservices.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.exceptions.HomeResourceAlreadyExistsException;
import com.sivvg.tradingservices.exceptions.ResourceNotFoundException;
import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.DailyTipRequest;
import com.sivvg.tradingservices.repository.DailyTipRepository;
import com.sivvg.tradingservices.service.DailyTipService;

@Service
public class DailyTipServiceImpl implements DailyTipService {

	private static final Logger logger = LoggerFactory.getLogger(DailyTipServiceImpl.class);

	@Autowired
	private DailyTipRepository repo;

	// 🔹 SAVE TIP (EQUITY / FUTURES / OPTIONS SAFE)
	@Override
	public DailyTipEntity saveTip(DailyTipRequest req) {

		logger.info("Save Daily Tip service started | stockCode={}, category={}", req.getStockCode(),
				req.getCategory());

		if (repo.existsByStockCode(req.getStockCode())) {
			logger.warn("Daily tip already exists | stockCode={}", req.getStockCode());
			throw new HomeResourceAlreadyExistsException("Stock with code " + req.getStockCode() + " already exists!");
		}

		DailyTipEntity tip = new DailyTipEntity();

		// ===== COMMON FIELDS =====
		tip.setStockCode(req.getStockCode());
		tip.setStockPrice(req.getStockPrice());
		tip.setStockType(req.getStockType());
		tip.setCategory(req.getCategory());
		tip.setPlanType(req.getPlanType());
		tip.setSectorType(req.getSectorType());

		tip.setPostedTime(LocalDateTime.now());
		tip.setCreatedAt(LocalDateTime.now());

		// 🔥 INITIAL PRICES
		tip.setPreviousPrice(req.getStockPrice());
		tip.setCurrentPrice(req.getStockPrice());
		tip.setPriceDifference(0.0);

		// ===== CATEGORY BASED LOGIC =====
		if (req.getCategory().equalsIgnoreCase("OPTIONS")) {

			logger.debug("Processing OPTIONS category logic");

			if (req.getTargetPrice() == null || req.getAtm() == null) {
				logger.error("OPTIONS validation failed | ATM or TargetPrice missing");
				throw new RuntimeException("ATM and TargetPrice required for OPTIONS");
			}

			tip.setOptionType(req.getOptionType());
			tip.setStrikePrice(req.getStrikePrice());
			tip.setAtm(req.getAtm());
			tip.setOtm(req.getOtm());
			tip.setTargetPrice(req.getTargetPrice());

			tip.setTargetReached(false);

			// Disable equity targets
			tip.setT1(null);
			tip.setT2(null);
			tip.setT3(null);

			tip.setT1Reached(false);
			tip.setT2Reached(false);
			tip.setT3Reached(false);

		} else { // ===== EQUITY / FUTURES =====

			logger.debug("Processing EQUITY / FUTURES category logic");

			if (req.getT1() == null || req.getT2() == null || req.getT3() == null) {
				logger.error("Equity/Futures validation failed | T1/T2/T3 missing");
				throw new RuntimeException("T1, T2, T3 required for Equity/Futures");
			}

			tip.setT1(req.getT1());
			tip.setT2(req.getT2());
			tip.setT3(req.getT3());

			tip.setT1Reached(false);
			tip.setT2Reached(false);
			tip.setT3Reached(false);

			// Clear options fields
			tip.setOptionType(null);
			tip.setStrikePrice(null);
			tip.setAtm(null);
			tip.setOtm(null);
			tip.setTargetPrice(null);
			tip.setTargetReached(false);
		}

		DailyTipEntity saved = repo.save(tip);

		logger.info("Daily tip saved successfully | tipId={}, stockCode={}", saved.getId(), saved.getStockCode());

		return saved;
	}

	// 🔹 GET ALL TIPS
	@Override
	public List<DailyTipEntity> getAllTips() {

		logger.info("Fetching all daily tips");

		List<DailyTipEntity> tips = repo.findAll();

		logger.info("Total daily tips fetched: {}", tips.size());
		return tips;
	}

	// 🔹 GET TIPS BY CATEGORY
	@Override
	public List<DailyTipEntity> getTipsByCategory(String category) {

		logger.info("Fetching daily tips by category | category={}", category);

		return repo.findByCategoryIgnoreCase(category);
	}

	// 🔹 UPDATE TIP
	@Override
	public DailyTipEntity updateTip(Long tipId, DailyTipRequest req) {

		logger.info("Update Daily Tip service started | tipId={}", tipId);

		DailyTipEntity tip = repo.findById(tipId).orElseThrow(() -> {
			logger.error("Daily tip not found for update | tipId={}", tipId);
			return new ResourceNotFoundException("Tip with ID " + tipId + " not found!");
		});

		// COMMON
		tip.setStockCode(req.getStockCode());
		tip.setStockPrice(req.getStockPrice());
		tip.setStockType(req.getStockType());
		tip.setCategory(req.getCategory());
		tip.setPlanType(req.getPlanType());
		tip.setSectorType(req.getSectorType());

		// Reset tracking
		tip.setPreviousPrice(req.getStockPrice());
		tip.setCurrentPrice(req.getStockPrice());
		tip.setPriceDifference(0.0);

		tip.setT1Reached(false);
		tip.setT2Reached(false);
		tip.setT3Reached(false);
		tip.setTargetReached(false);

		tip.setT1ReachedTime(null);
		tip.setT2ReachedTime(null);
		tip.setT3ReachedTime(null);

		// CATEGORY BASED UPDATE
		if (req.getCategory().equalsIgnoreCase("OPTIONS")) {

			logger.debug("Updating OPTIONS fields");

			tip.setOptionType(req.getOptionType());
			tip.setStrikePrice(req.getStrikePrice());
			tip.setAtm(req.getAtm());
			tip.setOtm(req.getOtm());
			tip.setTargetPrice(req.getTargetPrice());

			tip.setT1(null);
			tip.setT2(null);
			tip.setT3(null);

		} else {

			logger.debug("Updating EQUITY / FUTURES fields");

			tip.setT1(req.getT1());
			tip.setT2(req.getT2());
			tip.setT3(req.getT3());

			tip.setOptionType(null);
			tip.setStrikePrice(null);
			tip.setAtm(null);
			tip.setOtm(null);
			tip.setTargetPrice(null);
		}

		tip.setUpdatedAt(LocalDateTime.now());

		DailyTipEntity updated = repo.save(tip);

		logger.info("Daily tip updated successfully | tipId={}", updated.getId());
		return updated;
	}

	// 🔹 DELETE TIP
	@Override
	public void deleteTip(Long tipId) {

		logger.warn("Delete Daily Tip service called | tipId={}", tipId);

		DailyTipEntity tip = repo.findById(tipId).orElseThrow(() -> {
			logger.error("Daily tip not found for delete | tipId={}", tipId);
			return new ResourceNotFoundException("Tip with ID " + tipId + " not found!");
		});

		repo.delete(tip);

		logger.info("Daily tip deleted successfully | tipId={}", tipId);
	}

	// 🔹 GET SINGLE TIP STATUS
	@Override
	public DailyTipEntity getTipStatus(Long tipId) {

		logger.info("Fetching daily tip status | tipId={}", tipId);

		return repo.findById(tipId).orElseThrow(() -> {
			logger.error("Daily tip not found | tipId={}", tipId);
			return new ResourceNotFoundException("Tip with ID " + tipId + " not found!");
		});
	}
}
