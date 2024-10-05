package com.application.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.project.model.JudgmentDebtCalculation;

@Repository
public interface JudgmentDebtCalculationRepository extends JpaRepository<JudgmentDebtCalculation, Integer> {

	List<JudgmentDebtCalculation> findByUserIdOrderByCreatedDateDesc(Integer userId);

}