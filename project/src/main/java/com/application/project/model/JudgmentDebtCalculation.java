package com.application.project.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class JudgmentDebtCalculation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer userId;

	private String title;

	private Double principal;

	private String startDate;

	private String endDate;

	@ElementCollection
	private List<TableData> tableDatas;

	private Double total;

	private Double totalInterest;

	@Lob
	@Column(name = "latex_formula_variables", columnDefinition = "TEXT")
	private String latexFormulaVariables;

	@Lob
	@Column(name = "latex_formula_values", columnDefinition = "TEXT")
	private String latexFormulaValues;

	private LocalDateTime createdDate;

	public JudgmentDebtCalculation() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPrincipal() {
		return principal;
	}

	public void setPrincipal(Double principal) {
		this.principal = principal;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public List<TableData> getTableDatas() {
		return tableDatas;
	}

	public void setTableDatas(List<TableData> tableDatas) {
		this.tableDatas = tableDatas;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(Double totalInterest) {
		this.totalInterest = totalInterest;
	}

	public String getLatexFormulaVariables() {
		return latexFormulaVariables;
	}

	public void setLatexFormulaVariables(String latexFormulaVariables) {
		this.latexFormulaVariables = latexFormulaVariables;
	}

	public String getLatexFormulaValues() {
		return latexFormulaValues;
	}

	public void setLatexFormulaValues(String latexFormulaValues) {
		this.latexFormulaValues = latexFormulaValues;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Embeddable
	public static class TableData {

		private Double interestRate;

		private String fromDate;

		private String toDate;

		public TableData() {
		}

		public TableData(Double interestRate, String fromDate, String toDate) {
			this.interestRate = interestRate;

			this.fromDate = fromDate;

			this.toDate = toDate;
		}

		public Double getInterestRate() {
			return interestRate;
		}

		public void setInterestRate(Double interestRate) {
			this.interestRate = interestRate;
		}

		public String getFromDate() {
			return fromDate;
		}

		public void setFromDate(String fromDate) {
			this.fromDate = fromDate;
		}

		public String getToDate() {
			return toDate;
		}

		public void setToDate(String toDate) {
			this.toDate = toDate;
		}

	}

}
