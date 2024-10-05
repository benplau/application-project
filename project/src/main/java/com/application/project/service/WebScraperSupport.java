package com.application.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public abstract class WebScraperSupport {

	@Autowired
	private Environment environment;

	protected List<Page> pages;

	protected abstract List<Map<String, String>> performScrapingTask(Map<String, Object> input);

	protected BrowserContext setup(Playwright playwright) {
		try {
			Browser browser = playwright.chromium().launch(
					new BrowserType.LaunchOptions().setHeadless(environment.getProperty("headless", boolean.class)));

			BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions()
					.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
							+ "Chrome/109.0.0.0 Safari/537.36"));

			browserContext.setDefaultTimeout(600000);

			pages = new ArrayList<>();

			System.out.println("Browser setup complete.");

			return browserContext;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred when setting up playwright browser", e);
		}
	}

	protected Page openPageWithUrl(String url, BrowserContext browserContext) {
		if (browserContext == null) {
			throw new IllegalStateException("Browser has not been set up. Please call setup() first.");
		}

		Page newPage = browserContext.newPage();

		newPage.navigate(url);

		pages.add(newPage);

		System.out.println("Navigated to " + url);

		return newPage;
	}

}
