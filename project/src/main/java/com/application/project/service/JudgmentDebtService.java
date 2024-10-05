package com.application.project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.project.model.JudgmentDebtCalculation;
import com.application.project.model.JudgmentDebtRate;
import com.application.project.repository.JudgmentDebtCalculationRepository;
import com.application.project.repository.JudgmentDebtRatesRepository;

@Service
public class JudgmentDebtService {

	@Autowired
	private JudgmentDebtCalculationRepository m_judgmentDebtCalculationRepository;

	@Autowired
	private JudgmentDebtRatesRepository m_judgmentDebtRatesRepopsitory;

	public List<Map<String, String>> formatResults(List<Map<String, String>> results, LocalDate startDate,
			LocalDate endDate) {
		List<Map<String, String>> formatedResults = new ArrayList<>();

		if (startDate == null || endDate == null) {
			LocalDate effectiveDate = LocalDate.parse(results.get(0).get("effectiveDate"),
					DateTimeFormatter.ofPattern("dd-MM-yyyy"));

			formatedResults.add(Map.of("fromDate", effectiveDate.toString(), "toDate",
					effectiveDate.plusMonths(3).minusDays(1).toString(), "interestRate",
					results.get(0).get("interestRate")));
		} else {

			List<LocalDate> effectiveDates = new ArrayList<>();

			for (Map<String, String> result : results) {
				LocalDate effectiveDate = LocalDate.parse(result.get("effectiveDate"),
						DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				effectiveDates.add(effectiveDate);
			}

			List<List<LocalDate>> dateRanges = splitDateRange(startDate, endDate, effectiveDates);

			for (int i = 0; i < dateRanges.size(); i++) {
				List<LocalDate> range = dateRanges.get(i);

				LocalDate fromDate = range.get(0);

				LocalDate toDate = range.get(1);

				String interestRate = results.get(results.size() - 1 - i).get("interestRate");

				formatedResults.add(Map.of("interestRate", interestRate, "fromDate", fromDate.toString(), "toDate",
						toDate.toString()));
			}

		}

		return formatedResults;
	}

	public List<List<LocalDate>> splitDateRange(LocalDate startDate, LocalDate endDate, List<LocalDate> splitDates) {
		List<List<LocalDate>> subRanges = new ArrayList<>();

		splitDates.sort(LocalDate::compareTo);

		splitDates.remove(0);

		LocalDate currentStartDate = startDate;

		for (LocalDate splitDate : splitDates) {
			if (!splitDate.isBefore(currentStartDate)) {
				subRanges.add(Arrays.asList(currentStartDate, splitDate.minusDays(1)));
			}

			currentStartDate = splitDate;
		}

		if (!currentStartDate.isAfter(endDate)) {
			subRanges.add(Arrays.asList(currentStartDate, endDate));
		}

		return subRanges;
	}

	public Object saveCalculationRecord(JudgmentDebtCalculation entry) {
		entry.setCreatedDate(LocalDateTime.now());

		m_judgmentDebtCalculationRepository.save(entry);

		return Map.of("status", "SUCCESS");
	}

	public Object getCalculationRecord(Integer userId) {
		return m_judgmentDebtCalculationRepository.findByUserIdOrderByCreatedDateDesc(userId);
	}

	public void updateJudgmentDebtRates(List<Map<String, String>> items) {
		JudgmentDebtRate rate = m_judgmentDebtRatesRepopsitory.findFirstByOrderByEffectiveDateDesc();

		items.forEach(item -> {
			if ((rate == null) || (item.get("effectiveDate").compareTo(rate.getEffectiveDate()) > 0)) {
				JudgmentDebtRate newRate = new JudgmentDebtRate();

				newRate.setEffectiveDate(item.get("effectiveDate"));

				newRate.setInterestRate(item.get("interestRate"));

				m_judgmentDebtRatesRepopsitory.save(newRate);
			}
		});
	}

	public List<JudgmentDebtRate> getAllJudgmentDebtRates() {
		return m_judgmentDebtRatesRepopsitory.findAll();
	}

}
