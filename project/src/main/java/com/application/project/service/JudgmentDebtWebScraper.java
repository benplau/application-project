package com.application.project.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

@Service
public class JudgmentDebtWebScraper extends WebScraperSupport {

	@Override
	public List<Map<String, String>> performScrapingTask(Map<String, Object> input) {
		try (Playwright playwright = Playwright.create()) {
			List<Map<String, String>> result = new ArrayList<>();

			BrowserContext browserContext = setup(playwright);

			Page page = openPageWithUrl("https://www.judiciary.hk/en/court_services_facilities/interest_rate.html",
					browserContext);

			try (browserContext; page) {
				getLatestJudgementDebtRate(input, result, page, browserContext);
				return result;
			}
		} catch (Exception e) {
			return List.of(Map.of("error_message", e.getMessage()));
		}
	}

	private void getLatestJudgementDebtRate(Map<String, Object> input, List<Map<String, String>> result, Page page,
			BrowserContext browserContext) {
		LocalDate endDate = (LocalDate) input.get("endDate");

		LocalDate startDate = (LocalDate) input.get("startDate");

		page.waitForLoadState();

		page.waitForSelector("#article > div.content_c > table > tbody");

		List<String> interestRates = page.locator("#article > div.content_c > table > tbody > tr > td:nth-child(1)")
				.allTextContents();

		List<String> effectiveDatesStr = page.locator("#article > div.content_c > table > tbody > tr > td:nth-child(2)")
				.allTextContents();

		List<LocalDate> effectiveDates = effectiveDatesStr.stream()
				.map(dateStr -> LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")))
				.collect(Collectors.toList());

		Integer endDateEffectiveDateIndex = null;

		Integer startDateEffectiveDateIndex = null;

		if (endDate != null && startDate != null) {
			for (int i = 0; i < effectiveDates.size(); i++) {
				LocalDate currentEffectiveDate = effectiveDates.get(i);

				LocalDate nextEffectiveDate = (i + 1 < effectiveDates.size()) ? effectiveDates.get(i + 1) : null;

				if (endDateEffectiveDateIndex == null && endDate.isAfter(currentEffectiveDate)) {
					endDateEffectiveDateIndex = i;
				} else if (currentEffectiveDate.isAfter(endDate) && nextEffectiveDate != null
						&& nextEffectiveDate.isBefore(endDate)) {
					endDateEffectiveDateIndex = i + 1;
				}

				if (startDateEffectiveDateIndex == null && startDate.isAfter(currentEffectiveDate)) {
					startDateEffectiveDateIndex = i;
				} else if (currentEffectiveDate.isAfter(startDate) && nextEffectiveDate != null
						&& nextEffectiveDate.isBefore(startDate)) {
					startDateEffectiveDateIndex = i + 1;
				}

				if (endDateEffectiveDateIndex != null && startDateEffectiveDateIndex != null) {
					break;
				}
			}
		}

		if (endDateEffectiveDateIndex != null && startDateEffectiveDateIndex != null) {
			for (int i = endDateEffectiveDateIndex; i <= startDateEffectiveDateIndex; i++) {
				result.add(Map.of("interestRate", interestRates.get(i), "effectiveDate", effectiveDatesStr.get(i)));
			}
		} else {
			result.add(Map.of("interestRate", interestRates.get(0), "effectiveDate", effectiveDatesStr.get(0)));
		}
	}

	@SuppressWarnings("unused")
	private void getallJudgementDebtRates(List<Map<String, String>> result, Page page) {
		List<String> interestRates = page.locator("#article > div.content_c > table > tbody > tr > td:nth-child(1)")
				.allTextContents();

		List<String> effectiveDates = page.locator("#article > div.content_c > table > tbody > tr > td:nth-child(2)")
				.allTextContents();

		for (int i = 0; i < interestRates.size(); i++) {
			result.add(Map.of("interestRate", interestRates.get(i), "effectiveDate", effectiveDates.get(i)));
		}
	}

}
