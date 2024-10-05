package com.application.project.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.project.model.JudgmentDebtCalculation;
import com.application.project.service.JudgmentDebtService;
import com.application.project.service.JudgmentDebtWebScraper;

@RestController
@RequestMapping(path = "/judgment-debt")
public class JudgmentDebtController {

	@Autowired
	private JudgmentDebtWebScraper m_judgmentDebtWebScraper;

	@Autowired
	private JudgmentDebtService m_judgmentDebtService;

	@PostMapping("/fetch-latest-rate")
	public Object getLatestJudgementDebt(@RequestBody Map<String, Object> payload) {
		String startDateStr = (String) payload.get("startDate");

		String endDateStr = (String) payload.get("endDate");

		LocalDate startDate;
		if (startDateStr != null && !startDateStr.isEmpty()) {
			startDate = LocalDate.parse(startDateStr);
		} else {
			startDate = null;
		}

		LocalDate endDate;
		if (endDateStr != null && !endDateStr.isEmpty()) {
			endDate = LocalDate.parse(endDateStr);
		} else {
			endDate = null;
		}

		Map<String, Object> inputMap = new HashMap<>();
		if (endDate != null) {
			inputMap.put("endDate", endDate);
		}
		if (startDate != null) {
			inputMap.put("startDate", startDate);
		}

		List<Map<String, String>> results = m_judgmentDebtWebScraper.performScrapingTask(inputMap);

		m_judgmentDebtService.updateJudgmentDebtRates(results);

		List<Map<String, String>> formatedResults = m_judgmentDebtService.formatResults(results, startDate, endDate);

		String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		return Map.of("results", formatedResults, "currentDateTime", currentDateTime);
	}

	@PostMapping("/records/save")
	public Object saveCalculationRecord(@RequestBody JudgmentDebtCalculation entry) {
		return m_judgmentDebtService.saveCalculationRecord(entry);
	}

	@GetMapping("/records/get/{userId}")
	public Object getCalculationRecord(@PathVariable Integer userId) {
		return m_judgmentDebtService.getCalculationRecord(userId);
	}

	@GetMapping("/interest-rates/get")
	public Object getAllJudgmentDebtRates() {
		return m_judgmentDebtService.getAllJudgmentDebtRates();
	}

}
