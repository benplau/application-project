package com.application.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.application.project.model.JudgmentDebtRate;

public interface JudgmentDebtRatesRepository extends JpaRepository<JudgmentDebtRate, Integer> {

	JudgmentDebtRate findFirstByOrderByEffectiveDateDesc();

}
